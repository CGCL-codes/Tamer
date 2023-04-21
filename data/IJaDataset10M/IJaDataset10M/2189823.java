package app;

import java.io.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Terrain {

    public float minx, maxx, minz, maxz;

    private static final int MAP_SIZE = 128;

    private static final int STEP_SIZE = 1;

    private byte[] heightMap = new byte[MAP_SIZE * MAP_SIZE];

    private float scaleValue = .15f;

    private boolean zoomIn;

    private boolean zoomOut;

    float scaleXZ = 0.7f;

    float scaleY = 0.1f;

    float textureScale = 70f;

    private GLU glu = new GLU();

    private int terrainTexture;

    private int terrainDL;

    GL gl;

    public Terrain() {
        minx = -(MAP_SIZE / 2) * scaleXZ;
        maxx = (MAP_SIZE / 2) * scaleXZ;
        minz = -(MAP_SIZE / 2) * scaleXZ;
        maxz = (MAP_SIZE / 2) * scaleXZ;
        try {
            loadRawFile("media/terrain3.raw", heightMap);
        } catch (IOException e) {
            System.out.println("terrain map did not load...");
            throw new RuntimeException(e);
        }
    }

    public Terrain(GL inGL) {
        gl = inGL;
        terrainDL = gl.glGenLists(1);
        minx = -(MAP_SIZE / 2) * scaleXZ;
        maxx = (MAP_SIZE / 2) * scaleXZ;
        minz = -(MAP_SIZE / 2) * scaleXZ;
        maxz = (MAP_SIZE / 2) * scaleXZ;
        try {
            loadRawFile("media/terrain3.raw", heightMap);
        } catch (IOException e) {
            System.out.println("terrain map did not load...");
            throw new RuntimeException(e);
        }
        terrainTexture = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, terrainTexture);
        TextureReader.Texture texture = null;
        try {
            texture = TextureReader.readTexture("media/terrrain(256x256).jpg");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        makeRGBTexture(gl, glu, texture, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        createHeightMap();
    }

    private void loadRawFile(String strName, byte[] pHeightMap) throws IOException {
        InputStream input = ResourceRetriever.getResourceAsStream(strName);
        readBuffer(input, pHeightMap);
        input.close();
        for (int i = 0; i < pHeightMap.length; i++) pHeightMap[i] &= 0xFF;
    }

    private static void readBuffer(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        int bytesToRead = buffer.length;
        while (bytesToRead > 0) {
            int read = in.read(buffer, bytesRead, bytesToRead);
            bytesRead += read;
            bytesToRead -= read;
        }
    }

    public void renderHeightMap(GL gl) {
        gl.glPushMatrix();
        gl.glBindTexture(GL.GL_TEXTURE_2D, terrainTexture);
        gl.glScalef(scaleXZ, scaleY, scaleXZ);
        gl.glTranslatef(-MAP_SIZE / 2, 0, -MAP_SIZE / 2);
        gl.glCallList(terrainDL);
        gl.glPopMatrix();
    }

    private void createHeightMap() {
        terrainDL = gl.glGenLists(1);
        gl.glNewList(terrainDL, GL.GL_COMPILE);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Vector3[] vertex = new Vector3[4];
        Vector3[] texCoords = new Vector3[4];
        Vector3[] normals = new Vector3[4];
        gl.glEnable(GL.GL_LIGHTING);
        for (int X = 0; X < (MAP_SIZE - STEP_SIZE); X += STEP_SIZE) for (int Y = 0; Y < (MAP_SIZE - STEP_SIZE); Y += STEP_SIZE) {
            int x = X;
            int y = (int) (height(heightMap, X, Y));
            int z = Y;
            texCoords[0] = new Vector3(X / textureScale, Y / textureScale, 0);
            normals[0] = new Vector3(0.0f, 1.0f, 0.0f);
            vertex[0] = new Vector3(x, y, z);
            x = X;
            y = (int) (height(heightMap, X, Y + STEP_SIZE));
            z = Y + STEP_SIZE;
            texCoords[1] = new Vector3(X / textureScale, (Y + STEP_SIZE) / textureScale, 0);
            normals[1] = new Vector3(0.0f, 1.0f, 0.0f);
            vertex[1] = new Vector3(x, y, z);
            x = X + STEP_SIZE;
            y = (int) (height(heightMap, X + STEP_SIZE, Y + STEP_SIZE));
            z = Y + STEP_SIZE;
            texCoords[2] = new Vector3((X + STEP_SIZE) / textureScale, (Y + STEP_SIZE) / textureScale, 0);
            normals[2] = new Vector3(0.0f, 1.0f, 0.0f);
            vertex[2] = new Vector3(x, y, z);
            x = X + STEP_SIZE;
            y = (int) (height(heightMap, X + STEP_SIZE, Y));
            z = Y;
            texCoords[3] = new Vector3((X + STEP_SIZE) / textureScale, Y / textureScale, 0);
            normals[3] = new Vector3(0.0f, 1.0f, 0.0f);
            vertex[3] = new Vector3(x, y, z);
            int vertexID = 0;
            Vector3 tempVertex1 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, ((int) vertex[vertexID].x + STEP_SIZE) % MAP_SIZE, (int) vertex[vertexID].z)), vertex[vertexID].z);
            Vector3 tempVertex2 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, (int) vertex[vertexID].x, ((int) vertex[vertexID].z + STEP_SIZE) % MAP_SIZE)), vertex[vertexID].z + STEP_SIZE);
            Vector3 direction1 = tempVertex1;
            Vector3 direction2 = tempVertex2;
            direction1 = direction1.add(vertex[vertexID].multiply(-1));
            direction2 = direction2.add(vertex[vertexID].multiply(-1));
            normals[vertexID] = direction1.cross(direction2);
            normals[vertexID].normalize();
            vertexID = 1;
            tempVertex1 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, ((int) vertex[vertexID].x + STEP_SIZE) % MAP_SIZE, (int) vertex[vertexID].z)), vertex[vertexID].z);
            tempVertex2 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, (int) vertex[vertexID].x, ((int) vertex[vertexID].z + STEP_SIZE) % MAP_SIZE)), vertex[vertexID].z + STEP_SIZE);
            direction1 = tempVertex1;
            direction2 = tempVertex2;
            direction1 = direction1.add(vertex[vertexID].multiply(-1));
            direction2 = direction2.add(vertex[vertexID].multiply(-1));
            normals[vertexID] = direction1.cross(direction2);
            normals[vertexID].normalize();
            vertexID = 2;
            tempVertex1 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, ((int) vertex[vertexID].x + STEP_SIZE) % MAP_SIZE, (int) vertex[vertexID].z)), vertex[vertexID].z);
            tempVertex2 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, (int) vertex[vertexID].x, ((int) vertex[vertexID].z + STEP_SIZE) % MAP_SIZE)), vertex[vertexID].z + STEP_SIZE);
            direction1 = tempVertex1;
            direction2 = tempVertex2;
            direction1 = direction1.add(vertex[vertexID].multiply(-1));
            direction2 = direction2.add(vertex[vertexID].multiply(-1));
            normals[vertexID] = direction1.cross(direction2);
            normals[vertexID].normalize();
            vertexID = 3;
            tempVertex1 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, ((int) vertex[vertexID].x + STEP_SIZE) % MAP_SIZE, (int) vertex[vertexID].z)), vertex[vertexID].z);
            tempVertex2 = new Vector3(vertex[vertexID].x + STEP_SIZE, (int) (height(heightMap, (int) vertex[vertexID].x, ((int) vertex[vertexID].z + STEP_SIZE) % MAP_SIZE)), vertex[vertexID].z + STEP_SIZE);
            direction1 = tempVertex1;
            direction2 = tempVertex2;
            direction1 = direction1.add(vertex[vertexID].multiply(-1));
            direction2 = direction2.add(vertex[vertexID].multiply(-1));
            normals[vertexID] = direction1.cross(direction2);
            normals[vertexID].normalize();
            vertexID = 2;
            gl.glTexCoord2f(texCoords[vertexID].x, texCoords[vertexID].y);
            gl.glNormal3f(normals[vertexID].x, normals[vertexID].y, normals[vertexID].z);
            gl.glVertex3i((int) vertex[vertexID].x, (int) vertex[vertexID].y, (int) vertex[vertexID].z);
            vertexID = 3;
            gl.glTexCoord2f(texCoords[vertexID].x, texCoords[vertexID].y);
            gl.glNormal3f(normals[vertexID].x, normals[vertexID].y, normals[vertexID].z);
            gl.glVertex3i((int) vertex[vertexID].x, (int) vertex[vertexID].y, (int) vertex[vertexID].z);
            vertexID = 0;
            gl.glTexCoord2f(texCoords[vertexID].x, texCoords[vertexID].y);
            gl.glNormal3f(normals[vertexID].x, normals[vertexID].y, normals[vertexID].z);
            gl.glVertex3i((int) vertex[vertexID].x, (int) vertex[vertexID].y, (int) vertex[vertexID].z);
            vertexID = 1;
            gl.glTexCoord2f(texCoords[vertexID].x, texCoords[vertexID].y);
            gl.glNormal3f(normals[vertexID].x, normals[vertexID].y, normals[vertexID].z);
            gl.glVertex3i((int) vertex[vertexID].x, (int) vertex[vertexID].y, (int) vertex[vertexID].z);
            vertexID = 2;
            gl.glTexCoord2f(texCoords[vertexID].x, texCoords[vertexID].y);
            gl.glNormal3f(normals[vertexID].x, normals[vertexID].y, normals[vertexID].z);
            gl.glVertex3i((int) vertex[vertexID].x, (int) vertex[vertexID].y, (int) vertex[vertexID].z);
            vertexID = 0;
            gl.glTexCoord2f(texCoords[vertexID].x, texCoords[vertexID].y);
            gl.glNormal3f(normals[vertexID].x, normals[vertexID].y, normals[vertexID].z);
            gl.glVertex3i((int) vertex[vertexID].x, (int) vertex[vertexID].y, (int) vertex[vertexID].z);
        }
        gl.glEnd();
        gl.glEndList();
    }

    public float terrainIntersection(Vector3 pos) {
        Vector3 down = new Vector3(0, -1, 0);
        Vector3 position = new Vector3(pos.x, pos.y, pos.y);
        position.x /= scaleXZ;
        position.y /= scaleY;
        position.z /= scaleXZ;
        position.x += MAP_SIZE / 2;
        position.z += MAP_SIZE / 2;
        Vector3 position2 = new Vector3();
        float interpX = position.x % STEP_SIZE;
        float interpY = position.z % STEP_SIZE;
        interpX /= STEP_SIZE;
        interpY /= STEP_SIZE;
        position2.x = position.x / STEP_SIZE;
        position2.y = position.z / STEP_SIZE;
        position2.x = (float) Math.floor(position2.x);
        position2.y = (float) Math.floor(position2.y);
        position2.x *= STEP_SIZE;
        position2.y *= STEP_SIZE;
        int h1 = (int) (height(heightMap, (int) position2.x, (int) position2.y));
        int h4 = (int) (height(heightMap, (int) position2.x + STEP_SIZE, (int) position2.y));
        int h2 = (int) (height(heightMap, (int) position2.x, (int) position2.y + STEP_SIZE));
        int h3 = (int) (height(heightMap, (int) position2.x + STEP_SIZE, (int) position2.y + STEP_SIZE));
        float x = position.x - position2.x;
        float y = position.y - position2.y;
        if (position2.y / position2.x >= 1.0f) {
            float newHeightY = (float) h1 * (1 - interpY) + (float) h2 * (interpY);
            float newHeightX = newHeightY * (1 - interpX) + (float) h3 * (interpX);
            return scaleY * newHeightX;
        } else {
            float newHeightY = (float) h4 * (1 - interpY) + (float) h3 * (interpY);
            float newHeightX = newHeightY * interpX + (float) h1 * (1 - interpX);
            return scaleY * newHeightX;
        }
    }

    private void setVertexColor(GL gl, byte[] pHeightMap, int x, int y) {
        float fColor = -0.15f + (height(pHeightMap, x, y) / 256.0f);
        gl.glColor3f(0, 0, fColor);
    }

    private int height(byte[] pHeightMap, int X, int Y) {
        int x = X % MAP_SIZE;
        int y = Y % MAP_SIZE;
        return pHeightMap[x + (y * MAP_SIZE)] & 0xFF;
    }

    private void makeRGBTexture(GL gl, GLU glu, TextureReader.Texture img, int target, boolean mipmapped) {
        if (mipmapped) {
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        } else {
            gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
        }
    }

    private int genTexture(GL gl) {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        return tmp[0];
    }
}
