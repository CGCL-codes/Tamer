package engine.graphics.synthesis.texture;

import java.util.Random;
import engine.base.FMath;
import engine.base.Vector3;

/**
 * Implementation of the paper Improving Noise, 2002, Ken Perlin based on Ken Perlin's reference implementation.
 * 
 * @author Holger Dammertz
 */
public final class Noise3D_ImprovedPerlin {

    static final int permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };

    final int p[] = new int[256];

    ;

    float size;

    /**
	 * 
	 * @param size
	 * @param seed
	 *            if seed >= 0 it is given to new Random(seed); -1 uses the values from Ken Perlin's reference implementation
	 */
    public Noise3D_ImprovedPerlin(int size, int seed) {
        this.size = size;
        setSeed(seed);
    }

    public void setSeed(int seed) {
        if (seed < 0) {
            for (int i = 0; i < 256; i++) p[i] = permutation[i];
        } else {
            Random r = new Random(seed);
            for (int i = 0; i < 256; i++) p[i] = i;
            for (int i = 0; i < 256; i++) {
                int j = r.nextInt(256);
                int temp = p[i];
                p[i] = p[j];
                p[j] = temp;
            }
        }
    }

    /**
	 * @return a value between [-1,1]
	 */
    public float sample(Vector3 pos) {
        return sample3d(pos, 1.0f);
    }

    public float sample3d(Vector3 pos, float scale) {
        float x = pos.x * size * scale, y = pos.y * size * scale, z = pos.z * size * scale;
        int X = FMath.ffloor(x), Y = FMath.ffloor(y), Z = FMath.ffloor(z);
        x -= X;
        y -= Y;
        z -= Z;
        X &= 255;
        Y &= 255;
        Z &= 255;
        float u = fade(x), v = fade(y), w = fade(z);
        int A = perm(X) + Y, AA = perm(A) + Z, AB = perm(A + 1) + Z, B = perm(X + 1) + Y, BA = perm(B) + Z, BB = perm(B + 1) + Z;
        return lerp(w, lerp(v, lerp(u, grad(perm(AA), x, y, z), grad(perm(BA), x - 1, y, z)), lerp(u, grad(perm(AB), x, y - 1, z), grad(perm(BB), x - 1, y - 1, z))), lerp(v, lerp(u, grad(perm(AA + 1), x, y, z - 1), grad(perm(BA + 1), x - 1, y, z - 1)), lerp(u, grad(perm(AB + 1), x, y - 1, z - 1), grad(perm(BB + 1), x - 1, y - 1, z - 1))));
    }

    /**
	 * This method samples the perlin noise in a periodic way: the period can be given for each axis separately.
	 * To work correctly: x \in [0, nx) then periodX = nx; (smaller nx are possible but result in unnessecary repetion.
	 * Values larger then 256 don't change anything since the noise function has an inherent period of 256.
	 * 
	 * The periodicity is achieved by repeating the gradient vectors at the border of the [0, periodX] box
	 * This is described in "Texturing and Modelling - A Procedural Approach" on page 85 (top) 
	 * (See also http://drilian.com/category/development/graphics/procedural-textures/) 
	 * 
	 * @param pos
	 * @param periodX
	 * @param periodY
	 * @param periodZ
	 * @return
	 */
    public float sample3dPeriodic(Vector3 pos, int periodX, int periodY, int periodZ) {
        float x = pos.x * size, y = pos.y * size, z = pos.z * size;
        int X = FMath.ffloor(x), Y = FMath.ffloor(y), Z = FMath.ffloor(z);
        x -= X;
        y -= Y;
        z -= Z;
        X &= 255;
        Y &= 255;
        Z &= 255;
        int Ix = X % periodX;
        int Iy = Y % periodY;
        int Iz = Z % periodZ;
        int Jx = (Ix + 1) % periodX;
        int Jy = (Iy + 1) % periodY;
        int Jz = (Iz + 1) % periodZ;
        float u = fade(x), v = fade(y), w = fade(z);
        int A = perm(Ix), AA = perm(A + Iy), AB = perm((A + Jy)), B = perm(Jx), BA = perm(B + Iy), BB = perm((B + Jy));
        return lerp(w, lerp(v, lerp(u, grad(perm(AA + Iz), x, y, z), grad(perm(BA + Iz), x - 1, y, z)), lerp(u, grad(perm(AB + Iz), x, y - 1, z), grad(perm(BB + Iz), x - 1, y - 1, z))), lerp(v, lerp(u, grad(perm(AA + Jz), x, y, z - 1), grad(perm(BA + Jz), x - 1, y, z - 1)), lerp(u, grad(perm(AB + Jz), x, y - 1, z - 1), grad(perm(BB + Jz), x - 1, y - 1, z - 1))));
    }

    int perm(int idx) {
        return p[idx & 255];
    }

    static float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    static float grad(int hash, float x, float y, float z) {
        int h = hash & 15;
        float u = (h < 8) ? x : y;
        float v = (h < 4) ? y : ((h == 12 || h == 14) ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
