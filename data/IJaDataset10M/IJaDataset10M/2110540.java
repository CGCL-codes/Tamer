package artofillusion.animation;

import artofillusion.*;
import artofillusion.object.*;
import artofillusion.texture.*;
import artofillusion.ui.*;
import buoy.event.*;
import buoy.widget.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** This is a Track which controls the value of a texture parameter. */
public class TextureTrack extends Track {

    ObjectInfo info;

    Timecourse tc;

    int smoothingMethod;

    WeightTrack theWeight;

    TextureParameter param[];

    public TextureTrack(ObjectInfo info) {
        super("Texture");
        this.info = info;
        tc = new Timecourse(new Keyframe[0], new double[0], new Smoothness[0]);
        smoothingMethod = Timecourse.INTERPOLATING;
        theWeight = new WeightTrack(this);
        param = info.object.getParameters();
    }

    public void apply(double time) {
        ArrayKeyframe val = (ArrayKeyframe) tc.evaluate(time, smoothingMethod);
        if (val == null) return;
        TextureParameter texParam[] = info.object.getParameters();
        ParameterValue paramValue[] = info.object.getParameterValues();
        double weight = theWeight.getWeight(time);
        for (int i = 0; i < texParam.length; i++) for (int j = 0; j < param.length; j++) {
            if (!texParam[i].equals(param[j])) continue;
            double v;
            if (weight == 1.0) v = val.val[j]; else v = (1.0 - weight) * paramValue[i].getAverageValue() + weight * val.val[j];
            if (v < texParam[i].minVal) v = texParam[i].minVal;
            if (v > texParam[i].maxVal) v = texParam[i].maxVal;
            paramValue[i] = new ConstantParameterValue(v);
        }
        info.object.setParameterValues(paramValue);
    }

    public Track duplicate(Object obj) {
        TextureTrack t = new TextureTrack((ObjectInfo) obj);
        t.name = name;
        t.enabled = enabled;
        t.quantized = quantized;
        t.smoothingMethod = smoothingMethod;
        t.tc = tc.duplicate((ObjectInfo) obj);
        t.theWeight = (WeightTrack) theWeight.duplicate(t);
        t.param = param;
        return t;
    }

    public void copy(Track tr) {
        TextureTrack t = (TextureTrack) tr;
        name = t.name;
        enabled = t.enabled;
        quantized = t.quantized;
        smoothingMethod = t.smoothingMethod;
        tc = t.tc.duplicate(info);
        theWeight = (WeightTrack) t.theWeight.duplicate(this);
        param = t.param;
    }

    public double[] getKeyTimes() {
        return tc.getTimes();
    }

    public Timecourse getTimecourse() {
        return tc;
    }

    public void setKeyframe(double time, Keyframe k, Smoothness s) {
        tc.addTimepoint(k, time, s);
    }

    public Keyframe setKeyframe(double time, Scene sc) {
        TextureParameter texParam[] = info.object.getParameters();
        ParameterValue paramValue[] = info.object.getParameterValues();
        double d[] = new double[param.length];
        for (int i = 0; i < texParam.length; i++) for (int j = 0; j < param.length; j++) if (texParam[i].equals(param[j])) d[j] = paramValue[i].getAverageValue();
        Keyframe k = new ArrayKeyframe(d);
        tc.addTimepoint(k, time, new Smoothness());
        return k;
    }

    /** Set a keyframe at the specified time, based on the current state of the Scene,
      if and only if the Scene does not match the current state of the track.  Return
      the new Keyframe, or null if none was set. */
    public Keyframe setKeyframeIfModified(double time, Scene sc) {
        TextureParameter texParam[] = info.object.getParameters();
        ParameterValue paramValue[] = info.object.getParameterValues();
        double d[] = new double[param.length];
        boolean change = false;
        ArrayKeyframe key = (ArrayKeyframe) tc.evaluate(time, smoothingMethod);
        for (int i = 0; i < texParam.length; i++) for (int j = 0; j < param.length; j++) if (texParam[i].equals(param[j])) {
            d[j] = paramValue[i].getAverageValue();
            if (key == null || d[j] != key.val[j]) change = true;
        }
        if (change) {
            Keyframe k = new ArrayKeyframe(d);
            tc.addTimepoint(k, time, new Smoothness());
            return k;
        }
        return null;
    }

    public int moveKeyframe(int which, double time) {
        return tc.moveTimepoint(which, time);
    }

    public void deleteKeyframe(int which) {
        tc.removeTimepoint(which);
    }

    public boolean isNullTrack() {
        return (tc.getTimes().length == 0);
    }

    public Track[] getSubtracks() {
        return new Track[] { theWeight };
    }

    public boolean canAcceptAsParent(Object obj) {
        return (obj instanceof ObjectInfo);
    }

    public Object getParent() {
        return info;
    }

    public void setParent(Object obj) {
        info = (ObjectInfo) obj;
    }

    public int getSmoothingMethod() {
        return smoothingMethod;
    }

    public void setSmoothingMethod(int method) {
        smoothingMethod = method;
    }

    public String[] getValueNames() {
        String names[] = new String[param.length];
        for (int i = 0; i < names.length; i++) names[i] = param[i].name;
        return names;
    }

    public double[] getDefaultGraphValues() {
        TextureParameter texParam[] = info.object.getParameters();
        ParameterValue paramValue[] = info.object.getParameterValues();
        double d[] = new double[param.length];
        for (int i = 0; i < texParam.length; i++) for (int j = 0; j < param.length; j++) if (texParam[i].equals(param[j])) d[j] = paramValue[i].getAverageValue();
        return d;
    }

    public double[][] getValueRange() {
        double range[][] = new double[param.length][2];
        for (int i = 0; i < range.length; i++) {
            range[i][0] = param[i].minVal;
            range[i][1] = param[i].maxVal;
        }
        return range;
    }

    public ObjectInfo[] getDependencies() {
        return new ObjectInfo[0];
    }

    public void deleteDependencies(ObjectInfo obj) {
    }

    public void parametersChanged() {
        TextureParameter texParam[] = info.object.getParameters();
        boolean exists[] = new boolean[param.length];
        int num = 0;
        for (int i = 0; i < param.length; i++) for (int j = 0; j < texParam.length; j++) if (param[i].equals(texParam[j])) {
            exists[i] = true;
            num++;
            break;
        }
        TextureParameter newparam[] = new TextureParameter[num];
        for (int i = 0, j = 0; i < exists.length; i++) if (exists[i]) newparam[j++] = param[i];
        param = newparam;
        Keyframe key[] = tc.getValues();
        for (int k = 0; k < key.length; k++) {
            double newval[] = new double[num];
            for (int i = 0, j = 0; i < exists.length; i++) if (exists[i]) newval[j++] = ((ArrayKeyframe) key[k]).val[i];
            ((ArrayKeyframe) key[k]).val = newval;
        }
    }

    public void writeToStream(DataOutputStream out, Scene scene) throws IOException {
        TextureParameter texParam[] = info.object.getParameters();
        double t[] = tc.getTimes();
        Smoothness s[] = tc.getSmoothness();
        Keyframe v[] = tc.getValues();
        out.writeShort(0);
        out.writeUTF(name);
        out.writeBoolean(enabled);
        out.writeInt(smoothingMethod);
        out.writeShort(param.length);
        int index[] = new int[param.length];
        for (int i = 0; i < param.length; i++) for (int j = 0; j < texParam.length; j++) if (param[i].equals(texParam[j])) index[i] = j;
        for (int i = 0; i < index.length; i++) out.writeShort(index[i]);
        out.writeInt(t.length);
        for (int i = 0; i < t.length; i++) {
            out.writeDouble(t[i]);
            ((ArrayKeyframe) v[i]).writeToStream(out);
            s[i].writeToStream(out);
        }
        theWeight.writeToStream(out, scene);
    }

    /** Initialize this tracked based on its serialized representation as written by writeToStream(). */
    public void initFromStream(DataInputStream in, Scene scene) throws IOException, InvalidObjectException {
        short version = in.readShort();
        if (version != 0) throw new InvalidObjectException("");
        name = in.readUTF();
        enabled = in.readBoolean();
        smoothingMethod = in.readInt();
        int numParams = in.readShort();
        param = new TextureParameter[numParams];
        TextureParameter texParam[] = info.object.getParameters();
        for (int i = 0; i < param.length; i++) param[i] = texParam[in.readShort()];
        int keys = in.readInt();
        double t[] = new double[keys];
        Smoothness s[] = new Smoothness[keys];
        Keyframe v[] = new Keyframe[keys];
        for (int i = 0; i < keys; i++) {
            t[i] = in.readDouble();
            v[i] = new ArrayKeyframe(in, this);
            s[i] = new Smoothness(in);
        }
        tc = new Timecourse(v, t, s);
        theWeight.initFromStream(in, scene);
    }

    /** Present a window in which the user can edit the specified keyframe. */
    public void editKeyframe(LayoutWindow win, int which) {
        ArrayKeyframe key = (ArrayKeyframe) tc.getValues()[which];
        Smoothness s = tc.getSmoothness()[which];
        double time = tc.getTimes()[which];
        ValueField timeField = new ValueField(time, ValueField.NONE, 5);
        ValueSlider s1Slider = new ValueSlider(0.0, 1.0, 100, s.getLeftSmoothness());
        final ValueSlider s2Slider = new ValueSlider(0.0, 1.0, 100, s.getRightSmoothness());
        final BCheckBox sameBox = new BCheckBox(Translate.text("separateSmoothness"), !s.isForceSame());
        Widget widget[] = new Widget[param.length + 5];
        String label[] = new String[param.length + 5];
        for (int i = 0; i < param.length; i++) {
            widget[i] = param[i].getEditingWidget(key.val[i]);
            label[i] = param[i].name;
        }
        sameBox.addEventLink(ValueChangedEvent.class, new Object() {

            void processEvent() {
                s2Slider.setEnabled(sameBox.getState());
            }
        });
        s2Slider.setEnabled(sameBox.getState());
        int n = param.length;
        widget[n] = timeField;
        widget[n + 1] = sameBox;
        widget[n + 2] = new BLabel(Translate.text("Smoothness") + ':');
        widget[n + 3] = s1Slider;
        widget[n + 4] = s2Slider;
        label[n] = Translate.text("Time");
        label[n + 3] = "(" + Translate.text("left") + ")";
        label[n + 4] = "(" + Translate.text("right") + ")";
        ComponentsDialog dlg = new ComponentsDialog(win, Translate.text("editKeyframe"), widget, label);
        if (!dlg.clickedOk()) return;
        win.setUndoRecord(new UndoRecord(win, false, UndoRecord.COPY_TRACK, new Object[] { this, duplicate(info) }));
        for (int i = 0; i < param.length; i++) {
            if (widget[i] instanceof ValueField) key.val[i] = ((ValueField) widget[i]).getValue(); else key.val[i] = ((ValueSlider) widget[i]).getValue();
        }
        if (sameBox.getState()) s.setSmoothness(s1Slider.getValue(), s2Slider.getValue()); else s.setSmoothness(s1Slider.getValue());
        moveKeyframe(which, timeField.getValue());
    }

    /** This method presents a window in which the user can edit the track. */
    public void edit(LayoutWindow win) {
        BTextField nameField = new BTextField(getName());
        BComboBox smoothChoice = new BComboBox(new String[] { Translate.text("Discontinuous"), Translate.text("Linear"), Translate.text("Interpolating"), Translate.text("Approximating") });
        smoothChoice.setSelectedIndex(smoothingMethod);
        TreeList tree = new TreeList(win);
        BScrollPane sp = new BScrollPane(tree);
        Vector elements = new Vector();
        TextureParameter texParam[] = info.object.getParameters();
        ParameterValue paramValue[] = info.object.getParameterValues();
        if (info.object.getTextureMapping() instanceof LayeredMapping) {
            LayeredMapping map = (LayeredMapping) info.object.getTextureMapping();
            Texture layer[] = map.getLayers();
            for (int i = 0; i < layer.length; i++) {
                Vector v = new Vector();
                TextureParameter p[] = map.getLayerParameters(i);
                for (int j = 0; j < p.length; j++) {
                    int k;
                    for (k = 0; !texParam[k].equals(p[j]); k++) ;
                    if (!(paramValue[k] instanceof ConstantParameterValue)) continue;
                    TreeElement el = new GenericTreeElement(p[j].name, p[j].duplicate(), null, tree, null);
                    for (k = 0; k < param.length; k++) if (param[k].equals(p[j])) el.setSelected(true);
                    v.addElement(el);
                }
                if (v.size() == 0) {
                    TreeElement el = new GenericTreeElement(Translate.text("noAdjustableParams"), null, null, tree, null);
                    el.setSelectable(false);
                    v.addElement(el);
                }
                TreeElement el = new GenericTreeElement(Translate.text("layerLabel", Integer.toString(i + 1), layer[i].getName()), null, null, tree, v);
                el.setSelectable(false);
                el.setExpanded(true);
                elements.addElement(el);
            }
        } else for (int i = 0; i < texParam.length; i++) if (paramValue[i] instanceof ConstantParameterValue) elements.addElement(new GenericTreeElement(texParam[i].name, texParam[i], null, tree, null));
        if (elements.size() == 0) {
            TreeElement el = new GenericTreeElement(Translate.text("noAdjustableParams"), null, null, tree, null);
            el.setSelectable(false);
            elements.addElement(el);
        }
        TreeElement texElem = new GenericTreeElement(Translate.text("Texture"), null, null, tree, elements);
        texElem.setSelectable(false);
        texElem.setExpanded(true);
        tree.addElement(texElem);
        tree.setPreferredSize(new Dimension(150, 100));
        sp.setPreferredViewSize(new Dimension(150, 250));
        sp.setForceWidth(true);
        sp.setForceHeight(true);
        tree.setBackground(Color.white);
        ComponentsDialog dlg = new ComponentsDialog(win, Translate.text("paramTrackTitle"), new Widget[] { nameField, smoothChoice, Translate.label("selectTrackParams"), sp }, new String[] { Translate.text("trackName"), Translate.text("SmoothingMethod"), null, null });
        if (!dlg.clickedOk()) return;
        win.setUndoRecord(new UndoRecord(win, false, UndoRecord.COPY_OBJECT_INFO, new Object[] { info, info.duplicate() }));
        this.setName(nameField.getText());
        smoothingMethod = smoothChoice.getSelectedIndex();
        Object selected[] = tree.getSelectedObjects();
        int index[] = new int[selected.length];
        for (int i = 0; i < selected.length; i++) {
            index[i] = -1;
            for (int j = 0; j < param.length; j++) if (param[j].equals((TextureParameter) selected[i])) index[i] = j;
        }
        param = new TextureParameter[selected.length];
        System.arraycopy(selected, 0, param, 0, selected.length);
        Keyframe key[] = tc.getValues();
        for (int i = 0; i < key.length; i++) {
            double newval[] = new double[param.length];
            for (int j = 0; j < newval.length; j++) {
                if (index[j] > -1) newval[j] = ((ArrayKeyframe) key[i]).val[index[j]]; else for (int k = 0; k < texParam.length; k++) if (texParam[k].equals(param[j])) newval[j] = paramValue[k].getAverageValue();
            }
            ((ArrayKeyframe) key[i]).val = newval;
        }
    }
}
