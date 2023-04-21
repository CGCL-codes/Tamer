package andiamo;

import controlP5.ControlEvent;
import codeanticode.glgraphics.GLTexture;
import oscP5.*;

abstract class Layer implements Constants {

    String configFile;

    Andiamo andiamo;

    String name;

    Color tintColor;

    int index;

    boolean selected;

    boolean uiHidden;

    int type;

    GLTexture texture;

    GLTexture outTexture;

    AnchorList anchors;

    AFilterStack afilters;

    boolean grabKeyboard;

    Layer(Andiamo andiamo) {
        this.andiamo = andiamo;
        configFile = "";
        name = "";
        index = 0;
        type = -1;
        tintColor = new Color(andiamo, 0xFFFFFFFF);
        selected = false;
        uiHidden = false;
        grabKeyboard = false;
        anchors = new AnchorList(andiamo);
        afilters = null;
    }

    Layer(Andiamo andiamo, String filename, int idx) {
        this(andiamo);
        configFile = filename;
        index = idx;
    }

    void deleteAllInput() {
    }

    void stopAllMedia() {
    }

    void resetAllFilters() {
    }

    abstract void update();

    abstract void draw();

    abstract void drawOverlay();

    void initUI() {
    }

    void updateUI() {
    }

    void showUI() {
    }

    void hideUI() {
    }

    boolean insideUI() {
        return false;
    }

    void uiEvent(ControlEvent event, String name) {
    }

    void tabletMoved() {
    }

    void tabletPressed() {
    }

    void tabletDragged() {
    }

    void tabletReleased() {
    }

    void eraserPressed() {
    }

    void eraserReleased() {
    }

    void keyboardPressed(char key, int code) {
    }

    void keyboardReleased(char key, int code) {
    }

    void midiReceived(int param, int value) {
    }

    void oscReceived(OscMessage message) {
    }

    boolean selectAnchor(float x, float y, boolean create) {
        return anchors.select(x, y, create);
    }
}
