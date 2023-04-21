package andiamo;

import java.util.ArrayList;
import codeanticode.glgraphics.GLTexture;
import processing.core.PApplet;
import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import controlP5.Controller;
import controlP5.Tab;

class AnimationPicker extends Controller {

    float cX, cY;

    int numItems;

    int itemsPerRow = 4;

    float itemSize;

    int selItem;

    int numRows;

    int hoverItem;

    Andiamo andiamo;

    ArrayList<AnimationParameters> animations;

    float[] frameIdxs;

    AnimationPicker(ControlP5 theControlP5, String theName, int theX, int theY, int theWidth, Andiamo theAndiamo, ArrayList<AnimationParameters> theAnimations) {
        super(theControlP5, (Tab) (theControlP5.getTab("default")), theName, theX, theY, theWidth, 0);
        numItems = theAnimations.size();
        itemSize = theWidth / itemsPerRow;
        float f = (float) numItems / (float) itemsPerRow;
        float d = f - (int) f;
        if (PApplet.abs(d) < 0.001) numRows = (int) f; else numRows = (int) f + 1;
        int h = PApplet.ceil((int) (itemSize * numRows));
        setHeight(h);
        andiamo = theAndiamo;
        animations = theAnimations;
        frameIdxs = new float[numItems];
        for (int i = 0; i < numItems; i++) {
            frameIdxs[i] = 0;
        }
        _myArrayValue = new float[1];
        _myArrayValue[0] = selItem = 0;
        hoverItem = -1;
    }

    public void updateInternalEvents(PApplet theApplet) {
        if (getIsInside()) {
            cX = PApplet.constrain(theApplet.mouseX - position.x() - parent().absolutePosition().x, 0, width);
            cY = PApplet.constrain(theApplet.mouseY - position.y() - parent().absolutePosition().y, 0, height);
            int i = PApplet.floor(cX / itemSize);
            int j = PApplet.floor(cY / itemSize);
            int idx = itemsPerRow * j + i;
            if (isMousePressed && !controlP5.keyHandler.isAltDown) {
                if (idx < numItems) {
                    setValue(idx);
                }
            } else {
                if (idx < numItems) hoverItem = idx;
            }
        }
    }

    public void draw(PApplet theApplet) {
        theApplet.pushMatrix();
        theApplet.translate(position().x(), position().y());
        AnimationParameters aparams;
        GLTexture frame;
        int i, j;
        for (int n = 0; n < animations.size(); n++) {
            aparams = (AnimationParameters) animations.get(n);
            frame = aparams.frames[(int) (frameIdxs[n])];
            j = n / itemsPerRow;
            i = n % itemsPerRow;
            theApplet.image(frame, i * itemSize, j * itemSize, itemSize, itemSize);
            if (selItem == n) {
                theApplet.strokeWeight(4);
                theApplet.stroke(getColor().getActive());
                theApplet.noFill();
                theApplet.rect(i * itemSize, j * itemSize, itemSize, itemSize);
                theApplet.strokeWeight(1);
                theApplet.noStroke();
            }
        }
        theApplet.popMatrix();
    }

    public void setValue(float theValue) {
        selItem = (int) theValue;
        _myArrayValue[0] = selItem;
        broadcast(FLOAT);
    }

    public void updateAnimations() {
        AnimationParameters aparams;
        for (int i = 0; i < animations.size(); i++) {
            aparams = (AnimationParameters) animations.get(i);
            float frameFrac = aparams.fps / andiamo.chrono.fps;
            frameIdxs[i] += frameFrac;
            if (aparams.frames.length <= frameIdxs[i]) {
                frameIdxs[i] = 0.0f;
            }
        }
    }

    public void addToXMLElement(ControlP5XMLElement theElement) {
    }
}
