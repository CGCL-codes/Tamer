package de.grogra.imp3d.shading;

import java.awt.image.BufferedImage;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3f;
import de.grogra.math.Channel;
import de.grogra.math.ChannelData;
import de.grogra.math.ChannelMapInput;
import de.grogra.ray.physics.Environment;
import de.grogra.ray.physics.Spectrum;
import de.grogra.ray.physics.Spectrum3f;
import de.grogra.ray.util.Ray;
import de.grogra.ray.util.RayList;

public abstract class Material extends ColorMapNode implements Shader {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(Material.class);
        $TYPE.validate();
    }

    @Override
    protected void renderLine(BufferedImage image, int supersampling, int line, boolean useInput, java.util.Map cache) {
        Environment env = (Environment) cache.get("env");
        RayList rays = (RayList) cache.get("rays");
        if (env == null) {
            env = new Environment(null, new Spectrum3f(), Environment.STANDARD_RAY_TRACER);
            cache.put("env", env);
            rays = new RayList(env.tmpSpectrum0);
            cache.put("rays", rays);
            ChannelMapInput cmi = new ChannelMapInput();
            env.userObjectOwner = this;
            env.userObject = cmi;
            ChannelData src = cmi.getUserData(this, null), sink = src.createSink(this);
            sink.setProperty("ignoreInput", Boolean.valueOf(!useInput));
        }
        renderLine(this, env, rays, image, supersampling, line);
    }

    ChannelData getSource(Environment env) {
        if (!(env.userObject instanceof ChannelMapInput)) {
            env.userObjectOwner = this;
            env.userObject = new ChannelMapInput();
        }
        ChannelData src = ((ChannelMapInput) env.userObject).getUserData(this, null), sink = src.createSink(this);
        src.setFloat(Channel.IOR, env.iorRatio);
        src.setTuple3f(Channel.X, env.localPoint);
        src.setTuple3f(Channel.NX, env.normal);
        src.setTuple2f(Channel.U, env.uv);
        src.setTuple3f(Channel.DPXDU, env.dpdu);
        src.setTuple3f(Channel.DPXDV, env.dpdv);
        src.setTuple3f(Channel.PX, env.point);
        return src;
    }

    public static void renderLine(Shader sh, Environment env, RayList rays, BufferedImage image, int supersampling, int iz) {
        Vector3f sum = new Vector3f(), tmp = new Vector3f(), light = new Vector3f();
        light.set(-10, 3, 3);
        light.normalize();
        int sy = image.getWidth(), sz = image.getHeight();
        rays.setSize(2);
        Spectrum id = rays.rays[0].spectrum.newInstance();
        id.setIdentity();
        Tuple3d color = new Point3d();
        for (int iy = 0; iy < sy; iy++) {
            sum.set(0, 0, 0);
            for (int fy = 0; fy < supersampling; fy++) {
                float y = -1.1f * (2 * (supersampling * iy + fy) - sy * supersampling) / (sy * supersampling);
                for (int fz = 0; fz < supersampling; fz++) {
                    float z = -1.1f * (2 * (supersampling * iz + fz) - sz * supersampling) / (sz * supersampling);
                    float x = 1 - y * y - z * z;
                    float background = (((int) Math.floor((y + z) * 4) & 2) == 0) ? 0.8f : 0.5f;
                    if (x >= 0) {
                        x = (x <= 0) ? 0 : -(float) Math.sqrt(x);
                        tmp.set(x, y, z);
                        env.localPoint.set(tmp);
                        env.point.set(tmp);
                        env.normal.set(tmp);
                        float lightIntensity = (light.dot(tmp) >= 0) ? 1 : 0;
                        float u, cosu, sinu;
                        float sin = 1 - z * z;
                        if (sin < 1e-8) {
                            sin = 0;
                            u = 0;
                            cosu = 1;
                            sinu = 0;
                        } else {
                            sin = (float) Math.sqrt(sin);
                            u = (float) Math.atan2(y, x);
                            cosu = x / sin;
                            sinu = y / sin;
                        }
                        u /= 2 * Math.PI;
                        if (u < 0) {
                            u += 1;
                        }
                        float v = (float) (Math.acos(-z) / Math.PI);
                        env.uv.set(u, v);
                        env.dpdu.set((float) Math.PI * -2 * sinu * sin, (float) Math.PI * 2 * cosu * sin, 0);
                        env.dpdv.set((float) -Math.PI * cosu * z, (float) -Math.PI * sinu * z, (float) Math.PI * sin);
                        env.iorRatio = 1;
                        Ray r = rays.rays[0];
                        r.direction.set(light);
                        r.spectrum.setIdentity();
                        r.spectrum.scale(lightIntensity);
                        r = rays.rays[1];
                        r.direction.set(1, 0, 0);
                        r.spectrum.setIdentity();
                        r.spectrum.scale(background);
                        tmp.set(-1, 0, 0);
                        sh.shade(env, rays, tmp, id, color);
                        tmp.set(color);
                        sum.add(tmp);
                    } else {
                        tmp.set(background, background, background);
                        sum.add(tmp);
                    }
                }
            }
            sum.clamp(0, supersampling * supersampling);
            sum.scale(255.99f / (supersampling * supersampling));
            image.setRGB(iy, iz, (255 << 24) + ((int) sum.x << 16) + ((int) sum.y << 8) + (int) sum.z);
        }
    }
}
