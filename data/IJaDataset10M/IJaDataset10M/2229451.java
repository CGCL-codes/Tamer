package wingset;

import java.awt.event.*;
import java.util.*;
import org.wings.*;
import org.wings.border.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 1759 $
 */
public class Faces extends WingSetPane {

    static final ClassLoader cl = WingSet.class.getClassLoader();

    static final SIcon sel = new SResourceIcon(cl, "wingset/icons/RadioButtonSelectedIcon.gif");

    static final SIcon nsel = new SResourceIcon(cl, "wingset/icons/RadioButtonIcon.gif");

    static final SIcon pressed = new SResourceIcon(cl, "wingset/icons/RadioButtonPressedIcon.gif");

    static final SIcon rollsel = new SResourceIcon(cl, "wingset/icons/RadioButtonRolloverSelectedIcon.gif");

    static final SIcon rollnsel = new SResourceIcon(cl, "wingset/icons/RadioButtonRolloverIcon.gif");

    static final Face henner = new Face("Henner");

    static final Face armin = new Face("Armin");

    static final Face holger = new Face("Holger");

    static final Random random = new Random();

    static final int maxFaces = 10;

    ArrayList faces;

    SEmptyBorder nameBorder;

    SGridLayout layout;

    SPanel facePanel;

    SButtonGroup hairGroup;

    SButtonGroup eyeGroup;

    SButtonGroup mouthGroup;

    public SComponent createExample() {
        SPanel panel = new SPanel();
        panel.add(createSwitcher());
        return panel;
    }

    public SComponent createSwitcher() {
        nameBorder = new SEmptyBorder(10, 10, 10, 10);
        faces = new ArrayList();
        layout = new SGridLayout(4, faces.size() + 1);
        layout.setCellPadding(0);
        layout.setCellSpacing(0);
        facePanel = new SPanel(layout);
        final SLabel hair = new SLabel();
        hair.setImageAbsBottom(true);
        final SLabel eye = new SLabel();
        eye.setImageAbsBottom(true);
        final SLabel mouth = new SLabel();
        mouth.setImageAbsBottom(true);
        SForm shuffleForm = new SForm();
        SButton shuffleButton = new SButton("Shuffle");
        shuffleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                shuffle();
            }
        });
        shuffleForm.add(shuffleButton);
        facePanel.add(shuffleForm);
        facePanel.add(hair);
        facePanel.add(eye);
        facePanel.add(mouth);
        hairGroup = new SButtonGroup();
        hairGroup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                hair.setIcon(getFace(index).hair);
            }
        });
        eyeGroup = new SButtonGroup();
        eyeGroup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                eye.setIcon(getFace(index).eyes);
            }
        });
        mouthGroup = new SButtonGroup();
        mouthGroup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                mouth.setIcon(getFace(index).mouth);
            }
        });
        addFace(henner);
        addFace(armin);
        addFace(holger);
        shuffle();
        return facePanel;
    }

    protected void shuffle() {
        shuffle(hairGroup);
        shuffle(eyeGroup);
        shuffle(mouthGroup);
    }

    protected void shuffle(SButtonGroup g) {
        int selIndex = getRandomFaceIndex();
        for (Iterator iter = g.iterator(); iter.hasNext(); ) {
            if (selIndex == 0) {
                g.setSelected((SRadioButton) iter.next(), true);
                return;
            }
            iter.next();
            selIndex--;
        }
    }

    protected Face getFace(int index) {
        return (Face) faces.get(index);
    }

    public void addFace(Face f) {
        if (faces.size() > maxFaces) return;
        layout.setColumns(faces.size() + 2);
        SButton name = new SButton(f.name);
        name.setBorder(nameBorder);
        facePanel.add(name, faces.size() + 0 * (faces.size() + 2));
        final int faceNumber = faces.size();
        final SRadioButton hair = new SRadioButton();
        decorateButton(hair);
        hair.setActionCommand("" + faceNumber);
        hairGroup.add(hair);
        facePanel.add(hair, faces.size() + 1 * (faces.size() + 2));
        final SRadioButton eye = new SRadioButton();
        decorateButton(eye);
        eye.setActionCommand("" + faceNumber);
        eyeGroup.add(eye);
        facePanel.add(eye, faces.size() + 2 * (faces.size() + 2));
        final SRadioButton mouth = new SRadioButton();
        decorateButton(mouth);
        mouth.setActionCommand("" + faceNumber);
        mouthGroup.add(mouth);
        facePanel.add(mouth, faces.size() + 3 * (faces.size() + 2));
        faces.add(f);
        name.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hairGroup.setSelected(hair, true);
                eyeGroup.setSelected(eye, true);
                mouthGroup.setSelected(mouth, true);
            }
        });
    }

    void decorateButton(SRadioButton b) {
        b.setIcon(nsel);
        b.setSelectedIcon(sel);
        b.setRolloverIcon(rollnsel);
        b.setRolloverSelectedIcon(rollsel);
        b.setPressedIcon(pressed);
        b.setHorizontalAlignment(SRadioButton.CENTER);
        b.setVerticalAlignment(SRadioButton.CENTER);
    }

    int getRandomFaceIndex() {
        synchronized (random) {
            return random.nextInt(faces.size());
        }
    }

    static class Face {

        SIcon hair;

        SIcon eyes;

        SIcon mouth;

        String name;

        Face() {
        }

        Face(String name) {
            hair = new SResourceIcon(cl, "wingset/icons/" + name + "_hair.jpeg");
            eyes = new SResourceIcon(cl, "wingset/icons/" + name + "_eyes.jpeg");
            mouth = new SResourceIcon(cl, "wingset/icons/" + name + "_mouth.jpeg");
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
