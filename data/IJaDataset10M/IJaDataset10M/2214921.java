package com.g2d.studio.rpg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import com.cell.CIO;
import com.cell.rpg.formula.AbstractMethod;
import com.cell.rpg.formula.AbstractValue;
import com.cell.rpg.formula.Arithmetic;
import com.cell.rpg.formula.MathMethod;
import com.cell.rpg.formula.ObjectIDValue;
import com.cell.rpg.formula.StaticMethod;
import com.cell.rpg.formula.SystemMethod;
import com.cell.rpg.formula.TimeDurationValue;
import com.cell.rpg.formula.TimeValue;
import com.cell.rpg.formula.Value;
import com.cell.rpg.formula.AbstractMethod.MethodInfo;
import com.cell.rpg.formula.AbstractMethod.SyntheticMethod;
import com.cell.rpg.formula.Arithmetic.Operator;
import com.cell.rpg.formula.ObjectIDValue.ObjectType;
import com.cell.rpg.quest.TriggerUnitType;
import com.cell.rpg.quest.formula.QuestStateProperty;
import com.cell.rpg.quest.formula.TriggerUnitMethod;
import com.cell.rpg.quest.formula.TriggerUnitProperty;
import com.cell.rpg.quest.formula.QuestStateProperty.QuestStateField;
import com.cell.util.Pair;
import com.g2d.annotation.Property;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.awt.util.AbstractOptionDialog;
import com.g2d.editor.property.ListEnumEdit;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;
import com.g2d.studio.gameedit.ObjectSelectCellEditInteger;
import com.g2d.studio.gameedit.ObjectSelectComboInteger;
import com.g2d.studio.gameedit.XLSColumnSelectCellEdit;
import com.g2d.studio.gameedit.dynamic.DAvatar;
import com.g2d.studio.gameedit.dynamic.DEffect;
import com.g2d.studio.gameedit.template.XLSItem;
import com.g2d.studio.gameedit.template.XLSSkill;
import com.g2d.studio.gameedit.template.XLSUnit;
import com.g2d.studio.item.property.ItemPropertySavedTypeSelectComboBox;
import com.g2d.studio.quest.QuestSelectCellEditComboBox;
import com.g2d.studio.scene.editor.SceneListCellEditInteger;

public class FormulaEdit extends AbstractDialog implements PropertyCellEdit<AbstractValue>, ItemListener, ActionListener {

    Class<?>[] accept_types;

    AbstractValue value;

    JLabel edit_title = new JLabel();

    JComboBox types;

    JSplitPane split;

    JButton btn_ok = new JButton("确定");

    JButton btn_cancel = new JButton("取消");

    public FormulaEdit(Component owner, AbstractValue src) {
        this(owner, new Class<?>[] { Value.class, ObjectIDValue.class, TriggerUnitProperty.class, TriggerUnitMethod.class, QuestStateProperty.class, Arithmetic.class, MathMethod.class, SystemMethod.class, TimeValue.class, TimeDurationValue.class }, src);
    }

    public FormulaEdit(Component owner, Class<?>[] accept_types, AbstractValue src) {
        super(owner);
        super.setTitle("编辑变量");
        super.setSize(400, 300);
        this.accept_types = accept_types;
        this.value = src;
        ValueType[] value_types = new ValueType[accept_types.length];
        for (int i = 0; i < accept_types.length; i++) {
            value_types[i] = new ValueType(accept_types[i]);
        }
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        types = new JComboBox(value_types);
        types.addItemListener(this);
        split.setTopComponent(types);
        this.add(split, BorderLayout.CENTER);
        setValue(value);
        JPanel panel = new JPanel(new FlowLayout());
        btn_ok.addActionListener(this);
        btn_cancel.addActionListener(this);
        panel.add(btn_ok);
        panel.add(btn_cancel);
        this.add(panel, BorderLayout.SOUTH);
    }

    @Override
    public Component getComponent(ObjectPropertyEdit panel) {
        if (value != null) {
            edit_title.setText(value + "");
        } else {
            edit_title.setText("null");
        }
        return edit_title;
    }

    public AbstractValue getValue() {
        return value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_ok) {
            if (split.getBottomComponent() instanceof ValueEditor) {
                ValueEditor editor = (ValueEditor) split.getBottomComponent();
                if (editor.validateOK()) {
                    value = editor.onEditOK(value);
                    this.setVisible(false);
                }
            }
        } else if (e.getSource() == btn_cancel) {
            this.setVisible(false);
        }
    }

    public ValueType getValueType(Class<?> clzz) {
        for (int i = 0; i < types.getItemCount(); i++) {
            ValueType v = (ValueType) types.getItemAt(i);
            if (v.getKey().equals(clzz)) {
                return v;
            }
        }
        return null;
    }

    public ValueType getSelectedValueType() {
        return (ValueType) types.getSelectedItem();
    }

    public void setValue(AbstractValue src) {
        if (src != null) {
            this.value = src;
            ValueType type = getValueType(src.getClass());
            types.setSelectedItem(type);
            split.setBottomComponent((Component) type.getEditComponent());
        } else {
            try {
                Class<?> value_type = accept_types[0];
                this.value = (AbstractValue) (value_type.newInstance());
                ValueType type = getValueType(value_type);
                types.setSelectedItem(type);
                split.setBottomComponent((Component) type.getEditComponent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ValueType type = (ValueType) types.getSelectedItem();
            split.setBottomComponent((Component) type.getEditComponent());
        }
    }

    public void showDialog() {
        super.setVisible(true);
    }

    class ValueType extends Pair<Class<?>, Property> {

        ValueEditor edit_comp = null;

        public ValueType(Class<?> k) {
            super(k, k.getAnnotation(Property.class));
        }

        @Override
        public String toString() {
            return getValue().value()[0];
        }

        public ValueEditor getEditComponent() {
            if (edit_comp == null) {
                if (getKey().equals(Value.class)) {
                    edit_comp = new PanelValue(value);
                }
                if (getKey().equals(ObjectIDValue.class)) {
                    edit_comp = new PanelObjectIDValue(value);
                } else if (getKey().equals(TriggerUnitProperty.class)) {
                    edit_comp = new PanelUnitProperty(value);
                } else if (getKey().equals(TriggerUnitMethod.class)) {
                    edit_comp = new PanelUnitMethod(value);
                } else if (getKey().equals(QuestStateProperty.class)) {
                    edit_comp = new PanelQuestState(value);
                } else if (getKey().equals(Arithmetic.class)) {
                    edit_comp = new PanelArithmetic(value);
                } else if (getKey().equals(MathMethod.class)) {
                    edit_comp = new PanelStaticMethod<MathMethod>(value, MathMethod.class);
                } else if (getKey().equals(SystemMethod.class)) {
                    edit_comp = new PanelStaticMethod<SystemMethod>(value, SystemMethod.class);
                } else if (getKey().equals(TimeValue.class)) {
                    edit_comp = new PanelTimeValue(value);
                } else if (getKey().equals(TimeDurationValue.class)) {
                    edit_comp = new PanelTimeDurationValue(value);
                }
            }
            return edit_comp;
        }
    }

    interface ValueEditor {

        AbstractValue onEditOK(AbstractValue src_value);

        boolean validateOK();
    }

    static class PanelValue extends JPanel implements ValueEditor {

        JLabel lbl = new JLabel("值");

        JSpinner number = new JSpinner(new SpinnerNumberModel(1.0d, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));

        public PanelValue(AbstractValue value) {
            super.setLayout(new BorderLayout());
            super.add(lbl, BorderLayout.NORTH);
            super.add(number, BorderLayout.SOUTH);
            if (value instanceof Value) {
                number.setValue(((Value) value).value);
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            if (src_value instanceof Value) {
                ((Value) src_value).value = (Double) number.getValue();
                return src_value;
            } else {
                return new Value((Double) number.getValue());
            }
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class PanelObjectIDValue extends JPanel implements ValueEditor, ItemListener {

        ListEnumEdit<ObjectType> combo_obj_type = new ListEnumEdit<ObjectType>(ObjectType.class);

        ObjectSelectComboInteger<XLSUnit> combo_unit = new ObjectSelectComboInteger<XLSUnit>(XLSUnit.class);

        ObjectSelectComboInteger<XLSItem> combo_titem = new ObjectSelectComboInteger<XLSItem>(XLSItem.class);

        ObjectSelectComboInteger<XLSSkill> combo_skill = new ObjectSelectComboInteger<XLSSkill>(XLSSkill.class);

        ObjectSelectComboInteger<DAvatar> combo_avatar = new ObjectSelectComboInteger<DAvatar>(DAvatar.class);

        ObjectSelectComboInteger<DEffect> combo_effect = new ObjectSelectComboInteger<DEffect>(DEffect.class);

        QuestSelectCellEditComboBox combo_quest = new QuestSelectCellEditComboBox();

        SceneListCellEditInteger combo_scene = new SceneListCellEditInteger();

        ItemPropertySavedTypeSelectComboBox combo_itypes = new ItemPropertySavedTypeSelectComboBox(null);

        PropertyCellEdit<Integer> id_value;

        public PanelObjectIDValue(AbstractValue value) {
            super(new BorderLayout());
            super.add(combo_obj_type, BorderLayout.NORTH);
            if (value instanceof ObjectIDValue) {
                ObjectIDValue v = (ObjectIDValue) value;
                combo_obj_type.setValue(v.object_type);
            }
            setType(combo_obj_type.getValue());
            combo_obj_type.addItemListener(this);
        }

        private void setType(ObjectType type) {
            this.remove(combo_unit);
            this.remove(combo_titem);
            this.remove(combo_skill);
            this.remove(combo_avatar);
            this.remove(combo_effect);
            this.remove(combo_quest);
            this.remove(combo_scene);
            this.remove(combo_itypes);
            switch(type) {
                case TUNIT_ID:
                    id_value = combo_unit;
                    break;
                case TTEMPLATE_ITEM_ID:
                    id_value = combo_titem;
                    break;
                case TSKILL_ID:
                    id_value = combo_skill;
                    break;
                case TAVATAR_ID:
                    id_value = combo_avatar;
                    break;
                case TEFFECT_ID:
                    id_value = combo_effect;
                    break;
                case QUEST_ID:
                    id_value = combo_quest;
                    break;
                case SCENE_ID:
                    id_value = combo_scene;
                    break;
                case ITEM_PROPERTY_SAVED_TYPE:
                    id_value = combo_itypes;
                    break;
            }
            super.add((Component) id_value, BorderLayout.SOUTH);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == combo_obj_type) {
                    setType(combo_obj_type.getValue());
                }
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue srcValue) {
            ObjectIDValue ov = null;
            if (srcValue instanceof ObjectIDValue) {
                ov = (ObjectIDValue) srcValue;
            } else {
                ov = new ObjectIDValue();
            }
            ov.object_type = combo_obj_type.getValue();
            ov.object_id = id_value.getValue();
            return ov;
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class PanelStaticMethod<T extends StaticMethod> extends JPanel implements ValueEditor, ItemListener {

        protected final Class<T> value_type;

        MethodListCellEdit combo_methods;

        final MethodPanel method_panel;

        public PanelStaticMethod(AbstractValue value, Class<T> type) {
            super(new BorderLayout());
            this.value_type = type;
            this.combo_methods = new MethodListCellEdit(getMethods().values());
            super.add(combo_methods, BorderLayout.NORTH);
            AbstractMethod mm = null;
            if (type.isInstance(value)) {
                mm = (AbstractMethod) value;
                combo_methods.setSelectedItem(mm.getMethod());
            } else {
                try {
                    mm = type.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            method_panel = new MethodPanel(mm, getMethods().values().iterator().next());
            this.add(method_panel, BorderLayout.CENTER);
            combo_methods.addItemListener(this);
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            AbstractMethod mm = null;
            if (value_type.isInstance(src_value)) {
                mm = (AbstractMethod) src_value;
            } else {
                try {
                    mm = value_type.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return method_panel.getMethod(mm);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                method_panel.setMethod(combo_methods.getValue());
            }
        }

        @Override
        public boolean validateOK() {
            return true;
        }

        protected Map<MethodInfo, Method> getMethods() {
            if (value_type.equals(SystemMethod.class)) {
                return SystemMethod.getStaticMethods();
            }
            if (value_type.equals(MathMethod.class)) {
                return MathMethod.getStaticMethods();
            }
            return null;
        }
    }

    static class PanelArithmetic extends JPanel implements ValueEditor, ActionListener {

        JButton btn_left = new JButton();

        JComboBox btn_op = new ListEnumEdit<Operator>(Operator.class);

        JButton btn_right = new JButton();

        AbstractValue v_left;

        Operator v_op;

        AbstractValue v_right;

        public PanelArithmetic(AbstractValue value) {
            super(new FlowLayout());
            btn_left.addActionListener(this);
            btn_right.addActionListener(this);
            add(btn_left);
            add(btn_op);
            add(btn_right);
            if (value instanceof Arithmetic) {
                v_left = ((Arithmetic) value).left;
                v_op = ((Arithmetic) value).op;
                v_right = ((Arithmetic) value).right;
            } else {
                v_left = new Value(1);
                v_op = Operator.ADD;
                v_right = new Value(1);
            }
            btn_left.setText(v_left + "");
            btn_op.setSelectedItem(v_op);
            btn_right.setText(v_right + "");
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            v_op = (Operator) btn_op.getSelectedItem();
            if (src_value instanceof Arithmetic) {
                Arithmetic art = (Arithmetic) src_value;
                art.left = v_left;
                art.op = v_op;
                art.right = v_right;
                return art;
            } else {
                Arithmetic art = new Arithmetic();
                art.left = v_left;
                art.op = v_op;
                art.right = v_right;
                return art;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Window owner = AbstractDialog.getTopWindow(this);
            if (e.getSource() == btn_left) {
                FormulaEdit edit = new FormulaEdit(owner, v_left);
                edit.setLocation(owner.getX() + 20, owner.getY() + 20);
                edit.showDialog();
                v_left = edit.getValue();
                btn_left.setText(v_left + "");
            } else if (e.getSource() == btn_right) {
                FormulaEdit edit = new FormulaEdit(owner, v_right);
                edit.setLocation(owner.getX() + 20, owner.getY() + 20);
                edit.showDialog();
                v_right = edit.getValue();
                btn_right.setText(v_right + "");
            }
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class PanelUnitProperty extends JPanel implements ValueEditor, ItemListener {

        ListEnumEdit<TriggerUnitType> combo_unit_type = new ListEnumEdit<TriggerUnitType>(TriggerUnitType.class);

        XLSColumnSelectCellEdit combo_columns_player;

        XLSColumnSelectCellEdit combo_columns_unit;

        XLSColumnSelectCellEdit combo_columns_pet;

        public PanelUnitProperty(AbstractValue value) {
            super(new BorderLayout());
            combo_columns_player = new XLSColumnSelectCellEdit(Studio.getInstance().getObjectManager().getPlayerXLSColumns());
            combo_columns_unit = new XLSColumnSelectCellEdit(Studio.getInstance().getObjectManager().getUnitXLSColumns());
            combo_columns_pet = new XLSColumnSelectCellEdit(Studio.getInstance().getObjectManager().getPetXLSColumns());
            super.add(combo_unit_type, BorderLayout.NORTH);
            if (value instanceof TriggerUnitProperty) {
                TriggerUnitProperty tup = (TriggerUnitProperty) value;
                combo_columns_player.setSelectedItem(tup.filed_name);
                combo_columns_unit.setSelectedItem(tup.filed_name);
                combo_columns_pet.setSelectedItem(tup.filed_name);
                combo_unit_type.setSelectedItem(tup.trigger_unit_type);
            }
            if (combo_unit_type.getValue() == null) {
                combo_unit_type.setSelectedItem(TriggerUnitType.PLAYER);
            }
            changeUnitType(combo_unit_type.getValue());
            combo_unit_type.addItemListener(this);
        }

        void changeUnitType(TriggerUnitType type) {
            this.remove(combo_columns_player);
            this.remove(combo_columns_unit);
            switch(type) {
                case PLAYER:
                    super.add(combo_columns_player, BorderLayout.SOUTH);
                    break;
                case PET_GROUP:
                case ACTIVE_PET:
                    super.add(combo_columns_pet, BorderLayout.SOUTH);
                    break;
                case TRIGGERING_NPC:
                    super.add(combo_columns_unit, BorderLayout.SOUTH);
                    break;
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == combo_unit_type) {
                    changeUnitType(combo_unit_type.getValue());
                }
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            TriggerUnitProperty tup;
            if (src_value instanceof TriggerUnitProperty) {
                tup = (TriggerUnitProperty) src_value;
            } else {
                tup = new TriggerUnitProperty();
            }
            tup.trigger_unit_type = combo_unit_type.getValue();
            switch(tup.trigger_unit_type) {
                case PLAYER:
                    tup.filed_name = combo_columns_player.getValue();
                    break;
                case PET_GROUP:
                case ACTIVE_PET:
                    tup.filed_name = combo_columns_pet.getValue();
                    break;
                case TRIGGERING_NPC:
                    tup.filed_name = combo_columns_unit.getValue();
                    break;
            }
            return tup;
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class PanelUnitMethod extends JPanel implements ValueEditor, ItemListener {

        ListEnumEdit<TriggerUnitType> combo_unit_type = new ListEnumEdit<TriggerUnitType>(TriggerUnitType.class);

        MethodListCellEdit player_methods = new MethodListCellEdit(TriggerUnitMethod.getPlayerMethods().values());

        MethodListCellEdit pet_methods = new MethodListCellEdit(TriggerUnitMethod.getPetMethods().values());

        MethodListCellEdit npc_methods = new MethodListCellEdit(TriggerUnitMethod.getNpcMethods().values());

        JSplitPane method_pan = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        MethodPanel method_panel;

        public PanelUnitMethod(AbstractValue value) {
            super(new BorderLayout());
            super.add(combo_unit_type, BorderLayout.NORTH);
            super.add(method_pan, BorderLayout.CENTER);
            TriggerUnitMethod tum = null;
            if (value instanceof TriggerUnitMethod) {
                tum = (TriggerUnitMethod) value;
                combo_unit_type.setSelectedItem(tum.trigger_unit_type);
                player_methods.setSelectedItem(tum.getMethod());
                pet_methods.setSelectedItem(tum.getMethod());
                npc_methods.setSelectedItem(tum.getMethod());
            } else {
                tum = new TriggerUnitMethod();
            }
            method_panel = new MethodPanel(tum, player_methods.getValue());
            method_pan.setBottomComponent(method_panel);
            if (combo_unit_type.getValue() == null) {
                combo_unit_type.setSelectedItem(TriggerUnitType.PLAYER);
            }
            changeUnitType(combo_unit_type.getValue());
            combo_unit_type.addItemListener(this);
            player_methods.addItemListener(this);
            pet_methods.addItemListener(this);
            npc_methods.addItemListener(this);
        }

        void changeUnitType(TriggerUnitType type) {
            switch(type) {
                case PLAYER:
                    method_pan.setTopComponent(player_methods);
                    method_panel.setMethod(player_methods.getValue());
                    break;
                case PET_GROUP:
                    method_pan.setTopComponent(pet_methods);
                    method_panel.setMethod(pet_methods.getValue());
                    break;
                case ACTIVE_PET:
                    method_pan.setTopComponent(pet_methods);
                    method_panel.setMethod(pet_methods.getValue());
                    break;
                case TRIGGERING_NPC:
                    method_pan.setTopComponent(npc_methods);
                    method_panel.setMethod(npc_methods.getValue());
                    break;
            }
            System.out.println("changeUnitType " + type);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == combo_unit_type) {
                    changeUnitType(combo_unit_type.getValue());
                } else if (e.getSource() == player_methods) {
                    method_panel.setMethod(player_methods.getValue());
                } else if (e.getSource() == pet_methods) {
                    method_panel.setMethod(pet_methods.getValue());
                } else if (e.getSource() == npc_methods) {
                    method_panel.setMethod(npc_methods.getValue());
                }
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            TriggerUnitMethod tum;
            if (src_value instanceof TriggerUnitMethod) {
                tum = (TriggerUnitMethod) src_value;
            } else {
                tum = new TriggerUnitMethod();
            }
            tum.trigger_unit_type = combo_unit_type.getValue();
            return method_panel.getMethod(tum);
        }

        @Override
        public boolean validateOK() {
            if (!method_panel.validateReturnObject()) {
                JOptionPane.showMessageDialog(this, "没有设置复合类型的方法");
                return false;
            }
            return true;
        }
    }

    static class PanelQuestState extends JPanel implements ValueEditor {

        ListEnumEdit<QuestStateField> field = new ListEnumEdit<QuestStateField>(QuestStateField.class);

        QuestSelectCellEditComboBox quest = new QuestSelectCellEditComboBox();

        public PanelQuestState(AbstractValue value) {
            super.setLayout(new BorderLayout());
            super.add(quest, BorderLayout.NORTH);
            super.add(field, BorderLayout.SOUTH);
            if (value instanceof QuestStateProperty) {
                field.setSelectedItem(((QuestStateProperty) value).trigger_unit_field);
                quest.setValue(((QuestStateProperty) value).quest_id);
            } else {
                field.setSelectedItem(QuestStateField.ACCEPT_TIME_MS);
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            if (src_value instanceof QuestStateProperty) {
                ((QuestStateProperty) src_value).quest_id = quest.getValue();
                ((QuestStateProperty) src_value).trigger_unit_field = field.getValue();
                return src_value;
            } else {
                QuestStateProperty ret = new QuestStateProperty(quest.getValue(), field.getValue());
                return ret;
            }
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class PanelTimeValue extends JPanel implements ValueEditor {

        SpinnerDateModel model = new SpinnerDateModel();

        JSpinner spinner = new JSpinner(model);

        public PanelTimeValue(AbstractValue value) {
            super.setLayout(new BorderLayout());
            super.add(spinner, BorderLayout.NORTH);
            if (value instanceof TimeValue) {
                spinner.setValue(((TimeValue) value).getDate());
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue src_value) {
            if (src_value instanceof TimeValue) {
                ((TimeValue) src_value).setDate(model.getDate());
                return src_value;
            } else {
                return new TimeValue(model.getDate().getTime());
            }
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class PanelTimeDurationValue extends JPanel implements ValueEditor {

        ListEnumEdit<TimeUnit> time_unit = new ListEnumEdit<TimeUnit>(TimeUnit.class);

        SpinnerNumberModel model = new SpinnerNumberModel();

        JSpinner spinner = new JSpinner(model);

        public PanelTimeDurationValue(AbstractValue value) {
            super.setLayout(new BorderLayout());
            super.add(time_unit, BorderLayout.NORTH);
            super.add(spinner, BorderLayout.SOUTH);
            if (value instanceof TimeDurationValue) {
                time_unit.setSelectedItem(((TimeDurationValue) value).time_unit);
                model.setValue(((TimeDurationValue) value).duration);
            } else {
                time_unit.setSelectedItem(TimeUnit.MINUTES);
            }
        }

        @Override
        public AbstractValue onEditOK(AbstractValue srcValue) {
            if (srcValue instanceof TimeDurationValue) {
                ((TimeDurationValue) srcValue).duration = (Integer) model.getValue();
                ((TimeDurationValue) srcValue).time_unit = time_unit.getValue();
                return srcValue;
            } else {
                return new TimeDurationValue((Integer) model.getValue(), time_unit.getValue());
            }
        }

        @Override
        public boolean validateOK() {
            return true;
        }
    }

    static class SyntheticMethodEdit extends AbstractOptionDialog<SyntheticMethod> implements ItemListener {

        MethodListCellEdit method_list;

        MethodPanel method_panel;

        JButton ok = new JButton();

        public SyntheticMethodEdit(Component owner, Class<?> decaring_class, SyntheticMethod value) {
            super(owner);
            super.setLocation(owner.getX() + 20, owner.getY() + 20);
            super.setSize(300, 400);
            super.setTitle("复合类型 : " + decaring_class.getName());
            method_list = new MethodListCellEdit(AbstractMethod.getValidateMethods(decaring_class));
            if (value != null) {
                if (value.method_info.type.equals(decaring_class)) {
                    method_list.setSelectedItem(value.getMethod());
                } else {
                    value = new SyntheticMethod(method_list.getValue(), value.parameters);
                }
            } else {
                value = new SyntheticMethod(method_list.getValue(), null);
            }
            method_panel = new MethodPanel(value, method_list.getValue());
            super.add(method_list, BorderLayout.NORTH);
            super.add(method_panel, BorderLayout.CENTER);
            method_list.addItemListener(this);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == method_list) {
                    method_panel.setMethod(method_list.getValue());
                }
            }
        }

        @Override
        protected boolean checkOK() {
            if (!method_panel.validateReturnObject()) {
                JOptionPane.showMessageDialog(this, "没有设置复合类型的方法");
                return false;
            }
            return true;
        }

        @Override
        protected SyntheticMethod getUserObject(ActionEvent e) {
            SyntheticMethod srcValue = new SyntheticMethod();
            return (SyntheticMethod) method_panel.getMethod(srcValue);
        }
    }

    static class MethodPanel extends JPanel implements ActionListener {

        Method default_method;

        MethodInfo mirror_method;

        AbstractValue[] mirror_parameters;

        SyntheticMethod mirror_next_method;

        ArrayList<JButton> params_key = new ArrayList<JButton>();

        ArrayList<AbstractValue> params_value = new ArrayList<AbstractValue>();

        JButton next_method_btn = new JButton();

        public MethodPanel(AbstractMethod method, Method default_method) {
            super(new FlowLayout());
            this.default_method = default_method;
            this.mirror_parameters = CIO.cloneObject(method.parameters);
            this.mirror_next_method = CIO.cloneObject(method.return_object_method);
            next_method_btn.addActionListener(this);
            setMethod(method.getMethod());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Window owner = AbstractDialog.getTopWindow(this);
            if (params_key.contains(e.getSource())) {
                int index = params_key.indexOf(e.getSource());
                JButton key = (JButton) e.getSource();
                AbstractValue value = params_value.get(index);
                FormulaEdit edit = new FormulaEdit(owner, value);
                edit.setLocation(owner.getX() + 20, owner.getY() + 20);
                edit.showDialog();
                value = edit.getValue();
                params_value.set(index, value);
                key.setText(value + "");
            } else if (e.getSource() == next_method_btn) {
                SyntheticMethodEdit next_edit = new SyntheticMethodEdit(owner, mirror_method.method.getReturnType(), mirror_next_method);
                mirror_next_method = next_edit.showDialog();
                next_method_btn.setText(mirror_next_method + "");
                this.updateUI();
            }
        }

        public void setMethod(Method mt) {
            try {
                if (mt == null) {
                    mirror_method = new MethodInfo(default_method);
                } else {
                    mirror_method = new MethodInfo(mt);
                }
                this.remove(next_method_btn);
                Class<?>[] params_type = mirror_method.parameter_types;
                for (int i = 0; i < params_type.length; i++) {
                    if (i < params_key.size()) {
                        AbstractValue value = params_value.get(i);
                        JButton key = params_key.get(i);
                        key.setText(value.toString());
                    } else {
                        AbstractValue value;
                        if (mirror_parameters != null && i < mirror_parameters.length && mirror_parameters[i] != null) {
                            value = mirror_parameters[i];
                        } else {
                            value = new Value(1);
                        }
                        JButton key = new JButton(value.toString());
                        key.addActionListener(this);
                        params_key.add(key);
                        params_value.add(value);
                        this.add(key);
                    }
                }
                while (params_type.length < params_key.size()) {
                    int end = params_key.size() - 1;
                    JButton key = params_key.remove(end);
                    AbstractValue value = params_value.remove(end);
                    this.remove(key);
                    value.toString();
                }
                if (mirror_method.getReturnSynthetic() != null) {
                    if (mirror_next_method != null) {
                        next_method_btn.setText(mirror_next_method + "");
                    } else {
                        next_method_btn.setText("...");
                    }
                    this.add(next_method_btn);
                }
                this.updateUI();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        public AbstractMethod getMethod(AbstractMethod mm) {
            mirror_parameters = params_value.toArray(new AbstractValue[params_value.size()]);
            System.out.println(mirror_method);
            for (AbstractValue v : mirror_parameters) {
                System.out.println(v);
            }
            mm.method_info = mirror_method;
            mm.parameters = mirror_parameters;
            if (mirror_method.getReturnSynthetic() != null) {
                mm.return_object_method = mirror_next_method;
            } else {
                mm.return_object_method = null;
            }
            return mm;
        }

        public boolean validateReturnObject() {
            if (mirror_method.getReturnSynthetic() != null) {
                return mirror_next_method != null;
            }
            return true;
        }
    }
}
