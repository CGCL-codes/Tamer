package AGFX.F3D.Surface;

import java.util.ArrayList;
import AGFX.F3D.F3D;
import AGFX.F3D.Material.TF3D_Material;
import AGFX.F3D.Parser.TF3D_PARSER;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author AndyGFX
 * 
 */
public class TF3D_SurfaceManager {

    public ArrayList<TF3D_Material> materials;

    float matAmbient[] = new float[] { 0f, 0f, 0f, 1.0f };

    float matDiffuse[] = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

    float matSpecular[] = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

    public float WorldAmbient[] = new float[] { 0.3f, 0.3f, 0.3f, 1.0f };

    public TF3D_SurfaceManager() {
        F3D.Log.info("TF3D_SurfaceManager", "TA3D_SurfaceManager: constructor");
        this.materials = new ArrayList<TF3D_Material>();
        F3D.Log.info("TF3D_SurfaceManager", "TA3D_SurfaceManager: ... done");
    }

    public int Add(TF3D_Material mat) {
        if (this.Exist(mat.name)) {
            F3D.Log.info("TF3D_SurfaceManager", "TA3D_SurfaceManager: Add() '" + mat.name + "' wasn't added - exist !");
            return this.FindByName(mat.name);
        } else {
            int res = this.materials.size();
            this.materials.add(mat);
            F3D.Log.info("TF3D_SurfaceManager", "TA3D_SurfaceManager: Add() '" + mat.name + "'");
            return res;
        }
    }

    public int FindByName(String _name) {
        int res = -1;
        for (int i = 0; i < this.materials.size(); i++) {
            if (this.materials.get(i).name.equalsIgnoreCase(_name)) {
                res = i;
            }
        }
        return res;
    }

    public void ApplyMaterial(int id) {
        TF3D_Material mat = this.materials.get(id);
        this.ApplyMaterial(mat);
    }

    public void ApplyMaterial(TF3D_Material mat) {
        if (mat.typ == F3D.MAT_TYPE_TEXTURE) {
            this.ResetMaterial();
            if (mat.bAlphaTest) {
                glEnable(GL_ALPHA_TEST);
            } else {
                glDisable(GL_ALPHA_TEST);
            }
            if (mat.bDepthTest) {
                glEnable(GL_DEPTH_TEST);
            } else {
                glDisable(GL_DEPTH_TEST);
            }
            if (mat.bFaceCulling) {
                glEnable(GL_CULL_FACE);
            } else {
                glDisable(GL_CULL_FACE);
            }
            if (F3D.Config.use_gl_light) {
                glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT, F3D.GetBuffer.Float(mat.colors.ambient));
                glMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE, F3D.GetBuffer.Float(mat.colors.diffuse));
                glMaterial(GL_FRONT_AND_BACK, GL_SPECULAR, F3D.GetBuffer.Float(mat.colors.specular));
                glMaterial(GL_FRONT_AND_BACK, GL_EMISSION, F3D.GetBuffer.Float(mat.colors.emissive));
                glMaterialf(GL_FRONT, GL_SHININESS, mat.colors.shinisess);
            } else {
                glColor4f(mat.colors.diffuse[0], mat.colors.diffuse[1], mat.colors.diffuse[2], mat.colors.diffuse[3]);
            }
            for (int u = 0; u < F3D.MAX_TMU; u++) {
                if (mat.texture_unit[u].bEvent) {
                    F3D.MaterialEvents.Apply(u, mat.texture_unit[u].event_id);
                } else {
                    F3D.MaterialEvents.ResetEvent(u);
                }
                if (mat.texture_unit[u].bTexture) {
                    F3D.Textures.ActivateLayer(u);
                    F3D.Textures.Bind(mat.texture_unit[u].texture_id);
                } else {
                    F3D.Textures.DeactivateLayer(u);
                }
            }
        }
        if (mat.typ == F3D.MAT_TYPE_SHADER) {
            if (mat.bAlphaTest) {
                glEnable(GL_ALPHA_TEST);
            } else {
                glDisable(GL_ALPHA_TEST);
            }
            if (mat.bDepthTest) {
                glEnable(GL_DEPTH_TEST);
            } else {
                glDisable(GL_DEPTH_TEST);
            }
            if (mat.bFaceCulling) {
                glEnable(GL_CULL_FACE);
            } else {
                glDisable(GL_CULL_FACE);
            }
            glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT, F3D.GetBuffer.Float(mat.colors.ambient));
            glMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE, F3D.GetBuffer.Float(mat.colors.diffuse));
            glMaterial(GL_FRONT_AND_BACK, GL_SPECULAR, F3D.GetBuffer.Float(mat.colors.specular));
            glMaterial(GL_FRONT_AND_BACK, GL_EMISSION, F3D.GetBuffer.Float(mat.colors.emissive));
            glMaterialf(GL_FRONT, GL_SHININESS, mat.colors.shinisess);
            if (mat.use_shader) {
                F3D.Shaders.UseProgram(mat.shader_id);
            }
            for (int u = 0; u < F3D.MAX_TMU; u++) {
                if (mat.texture_unit[u].bEvent) {
                    F3D.MaterialEvents.Apply(u, mat.texture_unit[u].event_id);
                } else {
                    F3D.MaterialEvents.ResetEvent(u);
                }
                if (mat.texture_unit[u].bTexture) {
                    F3D.Textures.ActivateLayer(u);
                    F3D.Textures.Bind(mat.texture_unit[u].texture_id);
                } else {
                    F3D.Textures.DeactivateLayer(u);
                }
            }
        }
    }

    public void ResetMaterial() {
        F3D.Textures.DeactivateLayers();
        if ((F3D.Config.use_shaders) & (F3D.Extensions.GLSL_VertexShader) & (F3D.Extensions.GLSL_FragmenShader)) {
            F3D.Shaders.StopProgram();
        }
    }

    public void Load(String filename) {
        TF3D_PARSER PARSER = new TF3D_PARSER();
        TF3D_Material mat;
        int BLOCK_ID;
        String tmp_str;
        System.out.println("Loading config... " + filename);
        Boolean Exist = F3D.AbstractFiles.ExistFile(filename);
        if (!Exist) {
            System.out.print("Can't load file:" + filename);
        }
        PARSER.ParseFile(F3D.AbstractFiles.GetFullPath(filename));
        for (BLOCK_ID = 0; BLOCK_ID < PARSER.GetBlocksCount(); BLOCK_ID++) {
            PARSER.SetBlock(BLOCK_ID);
            mat = new TF3D_Material();
            tmp_str = PARSER.GetAs_STRING("type");
            if (tmp_str.equalsIgnoreCase("MAT_TEXTURE")) {
                mat.typ = F3D.MAT_TYPE_TEXTURE;
            }
            if (tmp_str.equalsIgnoreCase("MAT_SHADER")) {
                mat.typ = F3D.MAT_TYPE_SHADER;
                mat.shader_name = PARSER.GetAs_STRING("shader");
                mat.shader_id = F3D.Shaders.FindByName(mat.shader_name);
                if (mat.shader_id >= 0) {
                    mat.use_shader = true;
                }
            }
            mat.name = PARSER.GetAs_STRING("name");
            mat.colors.SetDiffuse(PARSER.GetAs_VECTOR4F("diffuse"));
            mat.colors.SetAmbient(PARSER.GetAs_VECTOR4F("ambient"));
            mat.colors.SetSpecular(PARSER.GetAs_VECTOR4F("specular"));
            mat.colors.SetEmissive(PARSER.GetAs_VECTOR4F("emissive"));
            mat.colors.shinisess = PARSER.GetAs_FLOAT("shinisess");
            if (mat.colors.shinisess > 128.0f) {
                mat.colors.shinisess = 127.0f;
            }
            mat.texture_unit[0].texture_name = PARSER.GetAs_STRING("texture_0");
            mat.texture_unit[1].texture_name = PARSER.GetAs_STRING("texture_1");
            mat.texture_unit[2].texture_name = PARSER.GetAs_STRING("texture_2");
            mat.texture_unit[3].texture_name = PARSER.GetAs_STRING("texture_3");
            mat.texture_unit[0].event_name = PARSER.GetAs_STRING("event_0");
            mat.texture_unit[1].event_name = PARSER.GetAs_STRING("event_1");
            mat.texture_unit[2].event_name = PARSER.GetAs_STRING("event_2");
            mat.texture_unit[3].event_name = PARSER.GetAs_STRING("event_3");
            mat.bDepthTest = PARSER.GetAs_BOOLEAN("depthtest");
            mat.bAlphaTest = PARSER.GetAs_BOOLEAN("alphatest");
            mat.bFaceCulling = PARSER.GetAs_BOOLEAN("faceculling");
            F3D.Log.warning("Culling", mat.name + " " + mat.bFaceCulling.toString());
            for (int t = 0; t < F3D.MAX_TMU; t++) {
                if (mat.texture_unit[t].texture_name.equals("none")) {
                    mat.texture_unit[t].bTexture = false;
                } else {
                    Boolean exist = F3D.Textures.Exist(mat.texture_unit[t].texture_name);
                    if (exist) {
                        mat.texture_unit[t].texture_id = F3D.Textures.FindByName(mat.texture_unit[t].texture_name);
                        mat.texture_unit[t].bTexture = true;
                    } else {
                        F3D.Log.error("TF3D_SurfaceManager", "Texture name: '" + mat.texture_unit[t].texture_name + "' missing (texture you have to load first !)");
                    }
                }
                if (mat.texture_unit[t].event_name.equalsIgnoreCase("none")) {
                    mat.texture_unit[t].bEvent = false;
                } else {
                    Boolean exist = F3D.MaterialEvents.Exist(mat.texture_unit[t].event_name);
                    if (exist) {
                        mat.texture_unit[t].event_id = F3D.MaterialEvents.FindByName(mat.texture_unit[t].event_name);
                        mat.texture_unit[t].bEvent = true;
                    } else {
                        F3D.Log.error("TF3D_SurfaceManager", "Event name: '" + mat.texture_unit[t].event_name + "' missing (events you have to load first !)");
                    }
                }
            }
            this.Add(mat);
        }
    }

    public Boolean Exist(String name) {
        Boolean res = false;
        int f = this.FindByName(name);
        if (f == -1) {
            res = false;
        } else {
            res = true;
        }
        return res;
    }

    public void Destroy() {
        for (int m = 0; m < this.materials.size(); m++) {
            this.materials.remove(m);
        }
    }
}
