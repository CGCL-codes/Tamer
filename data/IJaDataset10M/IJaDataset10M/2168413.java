package zildo.fwk.gfx.primitive;

import zildo.Zildo;
import zildo.fwk.opengl.compatibility.VBO;
import zildo.fwk.opengl.compatibility.VBOBuffers;
import zildo.monde.sprites.Reverse;

/**
 * @author Tchegito
 *
 */
public class QuadPrimitive {

    protected int nPoints;

    protected int nIndices;

    private boolean isLock;

    protected VBOBuffers bufs;

    protected VBO vbo;

    private int textureSizeX = 256;

    private int textureSizeY = 256;

    public QuadPrimitive(int numPoints) {
        initialize(numPoints);
    }

    public QuadPrimitive(int numPoints, int texSizeX, int texSizeY) {
        textureSizeX = texSizeX;
        textureSizeY = texSizeY;
        initialize(numPoints);
    }

    protected void initialize(int numPoints) {
        vbo = Zildo.pdPlugin.gfxStuff.createVBO();
        bufs = vbo.create(numPoints, false);
        nPoints = 0;
        nIndices = 0;
    }

    protected boolean isTiles() {
        return false;
    }

    public void startInitialization() {
        nPoints = 0;
        nIndices = 0;
    }

    public void endInitialization() {
        vbo.endInitialization(bufs);
    }

    public void cleanUp() {
        vbo.cleanUp(bufs);
        nPoints = 0;
        nIndices = 0;
    }

    public boolean isLock() {
        return isLock;
    }

    /**
     *  Move a tile and reset its texture (don't change size)<br/>
     * {@link #startInitialization()} should be called first.
     * @param x
     * @param y
     * @param u
     * @param v
     * @param reverse reverse attribute (horizonta and/or vertical)
     */
    public void updateQuad(float x, float y, float u, float v, Reverse reverse) {
        int vBufferPos = bufs.vertices.position();
        int tBufferPos = bufs.textures.position();
        if (bufs.vertices.limit() <= vBufferPos) {
            bufs.vertices.limit(vBufferPos + 2 * 4);
        }
        if (bufs.textures.limit() <= tBufferPos) {
            bufs.textures.limit(tBufferPos + 2 * 4);
        }
        float sizeX = bufs.vertices.get(vBufferPos + 2) - bufs.vertices.get(vBufferPos);
        float sizeY = bufs.vertices.get(vBufferPos + 2 * 2 + 1) - bufs.vertices.get(vBufferPos + 1);
        if (sizeX == 0) {
            sizeX = 16;
            sizeY = 16;
        }
        int revX = reverse.isHorizontal() ? -1 : 1;
        int revY = reverse.isVertical() ? -1 : 1;
        addSprite(x, y, u, v, sizeX * revX, sizeY * revY);
    }

    float[][] texCoords = new float[4][2];

    protected void putTexture(float xTex, float yTex, float sizeX, float sizeY, boolean six) {
        float texStartX = xTex;
        float texStartY = yTex;
        if (sizeX < 0) {
            texStartX -= sizeX;
        }
        if (sizeY < 0) {
            texStartY -= sizeY;
        }
        for (int i = 0; i < 4; i++) {
            float texPosX = texStartX + sizeX * (i % 2);
            float texPosY = texStartY + sizeY * (i / 2);
            texCoords[i][0] = texPosX / textureSizeX;
            texCoords[i][1] = texPosY / textureSizeY;
        }
        if (!six) {
            bufs.textures.put(texCoords[0][0]).put(texCoords[0][1]);
            bufs.textures.put(texCoords[1][0]).put(texCoords[1][1]);
            bufs.textures.put(texCoords[2][0]).put(texCoords[2][1]);
            bufs.textures.put(texCoords[3][0]).put(texCoords[3][1]);
        } else {
            bufs.textures.put(texCoords[0][0]).put(texCoords[0][1]);
            bufs.textures.put(texCoords[1][0]).put(texCoords[1][1]);
            bufs.textures.put(texCoords[2][0]).put(texCoords[2][1]);
            bufs.textures.put(texCoords[1][0]).put(texCoords[1][1]);
            bufs.textures.put(texCoords[3][0]).put(texCoords[3][1]);
            bufs.textures.put(texCoords[2][0]).put(texCoords[2][1]);
        }
    }

    protected int addQuadSized(int x, int y, float xTex, float yTex, int sizeX, int sizeY) {
        addSprite(x, y, xTex, yTex, sizeX, sizeY);
        return nPoints - 4;
    }

    float[][] vertices = new float[4][2];

    protected void addSprite(float x, float y, float xTex, float yTex, float sizeX, float sizeY) {
        if (bufs.vertices.position() == bufs.vertices.limit()) {
            bufs.vertices.limit(bufs.vertices.position() + 2 * 6);
            bufs.textures.limit(bufs.textures.position() + 2 * 6);
        }
        float pixSizeX = Math.abs(sizeX);
        float pixSizeY = Math.abs(sizeY);
        for (int i = 0; i < 4; i++) {
            vertices[i][0] = x + pixSizeX * (i % 2);
            vertices[i][1] = y + pixSizeY * (i / 2);
        }
        bufs.vertices.put(vertices[0][0]).put(vertices[0][1]);
        bufs.vertices.put(vertices[1][0]).put(vertices[1][1]);
        bufs.vertices.put(vertices[2][0]).put(vertices[2][1]);
        bufs.vertices.put(vertices[1][0]).put(vertices[1][1]);
        bufs.vertices.put(vertices[3][0]).put(vertices[3][1]);
        bufs.vertices.put(vertices[2][0]).put(vertices[2][1]);
        putTexture(xTex, yTex, sizeX, sizeY, true);
        nPoints += 4;
        nIndices += 6;
    }

    /**
     * Ask OpenGL to render every quad from this mesh.
     */
    public void render() {
        if (bufs.indices != null) {
            bufs.indices.limit(nIndices);
            vbo.draw(bufs);
        } else {
            vbo.draw(bufs, 0, nIndices);
        }
    }

    public boolean isEmpty() {
        return nPoints == 0;
    }
}
