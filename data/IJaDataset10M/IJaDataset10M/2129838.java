package ui.newui;

import com.toedter.calendar.JDateChooser;
import dbmanagement.*;
import dbreflection.tablemanagement.DatabaseObject;
import org.w3c.dom.Node;
import ui.MainView;
import ui.components.JJBlobView;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.*;

/**
 * �����-���
 * User: [J0k3r]
 * Date: 27.11.2008
 * Time: 0:22:42
 */
public class DBObjectPanelView extends UpdatablePanel {

    private HashMap<String, JComponent> paramValues = new HashMap<String, JComponent>();

    private HashMap<String, JComponent> need2BeFilled = new HashMap<String, JComponent>();

    private Integer objectId = null;

    private JButton complexObjectButton;

    private Set<String> shownParams = new HashSet<String>();

    private boolean saved = false;

    public DBObjectPanelView(final String layoutName, final FetchedObject inv, final boolean allowControls) {
        if (inv.getId() != null) objectId = inv.getId();
        this.setLayout(new BorderLayout(5, 5));
        try {
            JPanel mainPanel = new JPanel();
            mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            mainPanel.setLayout(new BorderLayout(5, 5));
            boolean found = false;
            if (!found) automaticLayout(inv, mainPanel, allowControls); else addUnshownParams(shownParams, inv, mainPanel, allowControls);
            this.add(mainPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "������ ����������� ���� ������ � ��������..");
        }
        JPanel butPan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        if (!inv.isDeleted()) ((JButton) butPan.add(new JButton((allowControls ? "������" : "��������� ���������"), new ImageIcon("./icons/disk.png")))).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    boolean errors = false;
                    ArrayList<String> keys = new ArrayList<String>(Arrays.asList(need2BeFilled.keySet().toArray(new String[need2BeFilled.size()])));
                    for (String s : paramValues.keySet()) {
                        if (inv.getParameter(s).getParamType().getName().equals("������") || inv.getParameter(s).getParamType().getName().equals("�����")) {
                            if ((need2BeFilled.containsKey(s)) && (((JTextComponent) paramValues.get(s)).getText().length() == 0)) {
                                errors = true;
                                paramValues.get(s).setBorder(new LineBorder(Color.red, 2, true));
                            } else {
                                inv.getParameter(s).setValue(((JTextComponent) paramValues.get(s)).getText());
                                paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                            }
                        }
                        if (inv.getParameter(s).getParamType().getName().equals("����� �����")) if ((need2BeFilled.containsKey(s)) && (((JTextComponent) paramValues.get(s)).getText().length() == 0)) {
                            errors = true;
                            paramValues.get(s).setBorder(new LineBorder(Color.red, 2, true));
                        } else {
                            if (((JTextComponent) paramValues.get(s)).getText().length() > 0) {
                                try {
                                    inv.getParameter(s).setValue(Integer.valueOf(((JTextComponent) paramValues.get(s)).getText()));
                                    paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                                } catch (NumberFormatException ex) {
                                    errors = true;
                                    paramValues.get(s).setBorder(new LineBorder(Color.blue, 2, true));
                                }
                            } else {
                                inv.getParameter(s).setValue(null);
                                paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                            }
                        }
                        if (inv.getParameter(s).getParamType().getName().equals("������������ �����")) if ((need2BeFilled.containsKey(s)) && (((JTextComponent) paramValues.get(s)).getText().length() == 0)) {
                            errors = true;
                            paramValues.get(s).setBorder(new LineBorder(Color.red, 2, true));
                        } else {
                            if (((JTextComponent) paramValues.get(s)).getText().length() > 0) {
                                try {
                                    inv.getParameter(s).setValue(Float.valueOf(((JTextComponent) paramValues.get(s)).getText()));
                                    paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                                } catch (NumberFormatException ex) {
                                    errors = true;
                                    paramValues.get(s).setBorder(new LineBorder(Color.blue, 2, true));
                                }
                            } else {
                                inv.getParameter(s).setValue(null);
                                paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                            }
                        }
                        if (inv.getParameter(s).getParamType().getName().equals("����")) if ((need2BeFilled.containsKey(s)) && (((JDateChooser) paramValues.get(s)).getDate() == null)) {
                            errors = true;
                            paramValues.get(s).setBorder(new LineBorder(Color.red, 2, true));
                        } else {
                            try {
                                inv.getParameter(s).setValue(((JDateChooser) paramValues.get(s)).getDate() != null ? new Timestamp(((JDateChooser) paramValues.get(s)).getDate().getTime()) : null);
                                paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                            } catch (NumberFormatException ex) {
                                errors = true;
                                paramValues.get(s).setBorder(new LineBorder(Color.blue, 2, true));
                            }
                        }
                        if (inv.getParameter(s).getParamType().getName().equals("��������")) {
                            Object value = ((JJBlobView) paramValues.get(s)).getValue();
                            if ((need2BeFilled.containsKey(s)) && (value == null)) {
                                errors = true;
                                paramValues.get(s).setBorder(new LineBorder(Color.red, 2, true));
                            } else {
                                if (((JJBlobView) paramValues.get(s)).getValue() != null) {
                                    if (value instanceof File) try {
                                        int length = (int) ((File) value).length();
                                        FileInputStream inputStream = new FileInputStream((File) value);
                                        byte[] bytes = new byte[length];
                                        inputStream.read(bytes);
                                        inv.getParameter(s).setValue(new SerialBlob(bytes));
                                        paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                                    } catch (NumberFormatException ex) {
                                        errors = true;
                                        paramValues.get(s).setBorder(new LineBorder(Color.blue, 2, true));
                                    }
                                } else {
                                    inv.getParameter(s).setValue(null);
                                    paramValues.get(s).setBorder(new LineBorder(Color.green, 1, true));
                                }
                            }
                        }
                    }
                    for (String key : keys) {
                        if (inv.getParameter(key).getParamType().getName().equals("��������")) {
                            if (((FetchedHierarchyParam) ((FetchedParam) inv.getParameter(key)).getValue()).getId() == null) {
                                errors = true;
                                need2BeFilled.get(key).setBorder(new LineBorder(Color.red, 2, true));
                            } else {
                                need2BeFilled.get(key).setBorder(new LineBorder(Color.green, 1, true));
                            }
                        }
                    }
                    if (!errors) {
                        if (allowControls) objectId = new DBObjectManager().saveFetchedObject(inv); else objectId = new DBObjectManager().updateFetchedObject(inv);
                        FetchedObject fo = new DBObjectManager().getObject(objectId);
                        MainView.updatePanels("ObjectList:" + layoutName, (allowControls ? UpdateTypes.CREATED : UpdateTypes.UPDATED), fo, null);
                        if (!allowControls) MainView.updatePanels("Object:" + fo.getId(), (allowControls ? UpdateTypes.CREATED : UpdateTypes.UPDATED), fo, null);
                        DBObjectPanelView.this.doUpdate((allowControls ? UpdateTypes.CREATED : UpdateTypes.UPDATED), fo);
                        saved = true;
                        closeParent(DBObjectPanelView.this);
                    } else JOptionPane.showMessageDialog(null, "����, ���������� �������, ������ ���� ���������.\n� �����, ���������� �����, �������� ������ ��������� �����.");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "������ ���������� ������");
                }
            }
        });
        if (!allowControls) {
            if (!inv.isDeleted()) ((JButton) butPan.add(complexObjectButton = new JButton("������� ������", new ImageIcon("./icons/application_link.png")))).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (inv != null) MainView.addFrame("������� ������: " + inv.getTemplateParams().get(0).getTemplate().getName() + " " + inv.getId(), new DBComplexObjectPanel(inv));
                }
            });
            ((JButton) butPan.add(new JButton("������� ���������", new ImageIcon("./icons/date_magnify.png")))).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (inv != null) MainView.addFrame("������� ��������� �������: " + inv.getTemplateParams().get(0).getTemplate().getName() + " " + inv.getId(), new ObjectChangeHistory(inv.getId()));
                }
            });
            complexObjectButton.requestFocus();
        }
        this.add(butPan, BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }

    /**
     * ���������� ��������� �������������..
     */
    private void automaticLayout(FetchedObject fo, JPanel panel, boolean allowControls) {
        ArrayList<String> mandatorySingleParams = new ArrayList<String>();
        ArrayList<String> mandatoryTextParams = new ArrayList<String>();
        ArrayList<String> singleParams = new ArrayList<String>();
        ArrayList<String> textParams = new ArrayList<String>();
        for (FetchedTemplateParams tParams : fo.getTemplateParams()) {
            for (FetchedParam param : tParams.getParams()) {
                if (param.isDeleted()) continue;
                if (param.isMandatory()) if (param.getParamType().getName().equals("�����")) mandatoryTextParams.add(param.getName()); else mandatorySingleParams.add(param.getName()); else {
                    if (param.getParamType().getName().equals("�����")) textParams.add(param.getName()); else singleParams.add(param.getName());
                }
            }
        }
        JPanel topSinglePan = new JPanel();
        topSinglePan.setLayout(new BoxLayout(topSinglePan, BoxLayout.Y_AXIS));
        JPanel topTextPan = new JPanel();
        topTextPan.setLayout(new BoxLayout(topTextPan, BoxLayout.Y_AXIS));
        JPanel centerPan = new JPanel();
        centerPan.setLayout(new BoxLayout(centerPan, BoxLayout.Y_AXIS));
        JPanel bottomSinglePan = new JPanel();
        bottomSinglePan.setLayout(new BoxLayout(bottomSinglePan, BoxLayout.Y_AXIS));
        JPanel bottomTextPan = new JPanel();
        bottomTextPan.setLayout(new BoxLayout(bottomTextPan, BoxLayout.Y_AXIS));
        boolean do2 = true;
        if ((mandatorySingleParams.size() > 0) || (mandatoryTextParams.size() > 0)) {
            for (String mandatorySingleParam : mandatorySingleParams) addParamOnPanel(fo, allowControls, mandatorySingleParam, topSinglePan);
            if (mandatoryTextParams.size() > 0) {
                for (String mandatoryTextParam : mandatoryTextParams) addParamOnPanel(fo, allowControls, mandatoryTextParam, centerPan);
                for (String singleParam : singleParams) addParamOnPanel(fo, allowControls, singleParam, bottomSinglePan);
                for (String textParam : textParams) addParamOnPanel(fo, allowControls, textParam, bottomTextPan);
                do2 = false;
            }
        }
        if (do2) {
            for (String singleParam : singleParams) addParamOnPanel(fo, allowControls, singleParam, topSinglePan);
            for (String textParam : textParams) addParamOnPanel(fo, allowControls, textParam, centerPan);
        }
        panel.setLayout(new BorderLayout(5, 5));
        if (topTextPan.getComponents().length > 0) {
            JPanel topPanel = new JPanel(new BorderLayout(5, 5));
            topPanel.add(topSinglePan, BorderLayout.NORTH);
            topPanel.add(topTextPan, BorderLayout.CENTER);
            panel.add(topPanel, BorderLayout.NORTH);
        } else panel.add(topSinglePan, BorderLayout.NORTH);
        if (centerPan.getComponents().length > 0) {
            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            center.add(centerPan);
            center.add(bottomTextPan);
            panel.add(center, BorderLayout.CENTER);
            panel.add(bottomSinglePan, BorderLayout.SOUTH);
        } else {
            if (bottomTextPan.getComponents().length > 0) {
                JPanel bottomPan = new JPanel(new BorderLayout(5, 5));
                bottomPan.add(bottomSinglePan, BorderLayout.NORTH);
                bottomPan.add(bottomTextPan, BorderLayout.CENTER);
                panel.add(bottomPan, BorderLayout.SOUTH);
            } else panel.add(bottomSinglePan, BorderLayout.SOUTH);
        }
    }

    private void addParamOnPanel(final FetchedObject fetchedObject, boolean allowControls, final String cell, JPanel paramsPanel) {
        JPanel paramPanel = new JPanel(new BorderLayout(5, 5));
        FetchedParam fetchedParam = fetchedObject.getParameter(cell);
        if (fetchedParam.getParamType().getName().equalsIgnoreCase("������") || fetchedParam.getParamType().getName().equalsIgnoreCase("�����") || fetchedParam.getParamType().getName().equalsIgnoreCase("������������ �����") || fetchedParam.getParamType().getName().equalsIgnoreCase("����� �����")) {
            if (!fetchedParam.getParamType().getName().equalsIgnoreCase("�����")) paramPanel.add(new JLabel(fetchedParam.getName() + (fetchedParam.isMandatory() ? " *" : "")), BorderLayout.WEST);
            JTextComponent field = (fetchedParam.getParamType().getName().equalsIgnoreCase("�����") ? new JTextArea(((fetchedParam.getValue() != null && (!DatabaseObject.isUnassigned(fetchedParam.getValue()))) ? fetchedParam.getValue().toString() : "")) : new JTextField(((fetchedParam.getValue() != null) && (!DatabaseObject.isUnassigned(fetchedParam.getValue())) ? fetchedParam.getValue().toString() : ""), 20));
            if (field instanceof JTextArea) {
                JScrollPane sp = new JScrollPane(field);
                sp.setBorder(new TitledBorder(cell + (fetchedParam.isMandatory() ? " *" : "")));
                paramPanel.add(sp, BorderLayout.CENTER);
            } else paramPanel.add(field, BorderLayout.CENTER);
            paramValues.put(cell, field);
            shownParams.add(cell);
            if (fetchedParam.isMandatory()) need2BeFilled.put(cell, field);
        }
        if (fetchedParam.getParamType().getName().equalsIgnoreCase("����")) {
            JPanel datePan = new JPanel(new BorderLayout(5, 5));
            datePan.add(new JLabel(cell + (fetchedParam.isMandatory() ? " *" : "")), BorderLayout.WEST);
            JDateChooser jc = new JDateChooser();
            if ((fetchedParam.getValue() != null) && (!DatabaseObject.isUnassigned(fetchedParam.getValue()))) jc.setDate(new Date(((Timestamp) fetchedParam.getValue()).getTime()));
            paramPanel.add(jc, BorderLayout.CENTER);
            paramValues.put(cell, jc);
            shownParams.add(cell);
            if (fetchedParam.isMandatory()) need2BeFilled.put(cell, jc);
        }
        if (fetchedParam.getParamType().getName().equalsIgnoreCase("��������")) {
            JJBlobView blobView = new JJBlobView(cell + (fetchedParam.isMandatory() ? " *" : ""), "", (Blob) fetchedParam.getValue());
            paramPanel.add(blobView, BorderLayout.CENTER);
            paramValues.put(cell, blobView);
            shownParams.add(cell);
            if (fetchedParam.isMandatory()) need2BeFilled.put(cell, blobView);
        }
        if (fetchedParam.getParamType().getName().equalsIgnoreCase("��������")) {
            paramPanel.add(new JLabel(cell + (fetchedParam.isMandatory() ? " *" : "")), BorderLayout.WEST);
            final FetchedHierarchyParam fetchedHParam = (FetchedHierarchyParam) fetchedParam.getValue();
            final JTextArea hierarchyList = new JTextArea(1, 20);
            final JScrollPane comp = new JScrollPane(hierarchyList);
            hierarchyList.setEditable(false);
            if (((FetchedHierarchyParam) fetchedParam.getValue()).getObject() != null) {
                DBObjectManager om = new DBObjectManager();
                try {
                    String text = "";
                    ArrayList<FetchedHierarchyParam> fetchedHierarchyParamArrayList = om.getHierarchy(fetchedHParam);
                    for (FetchedHierarchyParam fetchedHierarchyParam : fetchedHierarchyParamArrayList) text += (text.length() > 0 ? "\n" : "") + fetchedHierarchyParam.getObject().toString();
                    hierarchyList.setText(text);
                    comp.setPreferredSize(new Dimension((int) comp.getSize().getWidth(), (int) hierarchyList.getPreferredSize().getHeight() + (int) (hierarchyList.getPreferredSize().getHeight() / hierarchyList.getLineCount())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            comp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            paramPanel.add(comp, BorderLayout.CENTER);
            if (fetchedParam.isMandatory()) need2BeFilled.put(cell, hierarchyList);
            JButton bSelectHierarchy = new JButton(new ImageIcon("./icons/magnifier.png"));
            bSelectHierarchy.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    final HierarchySelecter selecter = new HierarchySelecter(fetchedHParam.getHierarchyId());
                    MainView.addFrame("������ �� ��������", selecter).addInternalFrameListener(new InternalFrameAdapter() {

                        public void internalFrameClosed(InternalFrameEvent e) {
                            if (selecter.getValue() == null) return;
                            FetchedHierarchyParam param = new FetchedHierarchyParam();
                            param.setHierarchyId(selecter.getValue().getHierarchy());
                            param.setId(selecter.getValue().getId());
                            fetchedObject.getParameter(cell).setValue(param);
                            DBObjectManager om = new DBObjectManager();
                            try {
                                String text = "";
                                ArrayList<FetchedHierarchyParam> fetchedHierarchyParamArrayList = om.getHierarchy(param);
                                for (FetchedHierarchyParam fetchedHierarchyParam : fetchedHierarchyParamArrayList) text += (text.length() > 0 ? "\n" : "") + fetchedHierarchyParam.getObject().toString();
                                hierarchyList.setText(text);
                                comp.setPreferredSize(new Dimension((int) comp.getSize().getWidth(), (int) hierarchyList.getPreferredSize().getHeight() + (int) (hierarchyList.getPreferredSize().getHeight() / hierarchyList.getLineCount())));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
            });
            paramPanel.add(bSelectHierarchy, BorderLayout.EAST);
            shownParams.add(cell);
        }
        paramsPanel.add(paramPanel);
    }

    private void addUnshownParams(Set<String> shownParams, final FetchedObject inv, JPanel panel, boolean allowControls) {
        JPanel paramsPanel = new JPanel();
        paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));
        for (FetchedTemplateParams fetchedTemplateParams : inv.getTemplateParams()) {
            for (FetchedParam fetchedParam : fetchedTemplateParams.getParams()) {
                final String cell = fetchedParam.getName();
                if (shownParams.contains(cell)) continue;
                JPanel paramPanel = new JPanel(new BorderLayout(5, 5));
                if (fetchedParam.getParamType().getName().equalsIgnoreCase("������") || fetchedParam.getParamType().getName().equalsIgnoreCase("�����") || fetchedParam.getParamType().getName().equalsIgnoreCase("������������ �����") || fetchedParam.getParamType().getName().equalsIgnoreCase("����� �����")) {
                    if (!fetchedParam.getParamType().getName().equalsIgnoreCase("�����")) paramPanel.add(new JLabel(fetchedParam.getName()), BorderLayout.WEST);
                    JTextComponent field = (fetchedParam.getParamType().getName().equalsIgnoreCase("�����") ? new JTextArea(((fetchedParam.getValue() != null && (!DatabaseObject.isUnassigned(fetchedParam.getValue()))) ? fetchedParam.getValue().toString() : "")) : new JTextField(((fetchedParam.getValue() != null) && (!DatabaseObject.isUnassigned(fetchedParam.getValue())) ? fetchedParam.getValue().toString() : ""), 20));
                    if (field instanceof JTextArea) {
                        JScrollPane sp = new JScrollPane(field);
                        sp.setBorder(new TitledBorder(cell));
                        paramPanel.add(sp, BorderLayout.CENTER);
                    } else paramPanel.add(field, BorderLayout.CENTER);
                    if (!allowControls) field.setEditable(false);
                    paramValues.put(cell, field);
                    shownParams.add(cell);
                }
                if (fetchedParam.getParamType().getName().equalsIgnoreCase("��������")) {
                    paramPanel.add(new JLabel(cell), BorderLayout.WEST);
                    final FetchedHierarchyParam fetchedHParam = (FetchedHierarchyParam) fetchedParam.getValue();
                    final JTextArea hierarchyList = new JTextArea();
                    hierarchyList.setEditable(false);
                    if (((FetchedHierarchyParam) fetchedParam.getValue()).getObject() != null) {
                        DBObjectManager om = new DBObjectManager();
                        try {
                            String text = "";
                            ArrayList<FetchedHierarchyParam> fetchedHierarchyParamArrayList = om.getHierarchy(fetchedHParam);
                            for (FetchedHierarchyParam fetchedHierarchyParam : fetchedHierarchyParamArrayList) text += (text.length() > 0 ? "\n" : "") + fetchedHierarchyParam.getObject().toString();
                            hierarchyList.setText(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    paramPanel.add(hierarchyList, BorderLayout.CENTER);
                    JButton bSelectHierarchy = new JButton("...");
                    if (!allowControls) bSelectHierarchy.setEnabled(false);
                    bSelectHierarchy.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            final HierarchySelecter selecter = new HierarchySelecter(fetchedHParam.getHierarchyId());
                            MainView.addFrame("������ �� ��������", selecter).addInternalFrameListener(new InternalFrameAdapter() {

                                public void internalFrameClosed(InternalFrameEvent e) {
                                    if (selecter.getValue() == null) return;
                                    FetchedHierarchyParam param = new FetchedHierarchyParam();
                                    param.setHierarchyId(selecter.getValue().getHierarchy());
                                    param.setId(selecter.getValue().getId());
                                    inv.getParameter(cell).setValue(param);
                                    DBObjectManager om = new DBObjectManager();
                                    try {
                                        String text = "";
                                        ArrayList<FetchedHierarchyParam> fetchedHierarchyParamArrayList = om.getHierarchy(param);
                                        for (FetchedHierarchyParam fetchedHierarchyParam : fetchedHierarchyParamArrayList) text += (text.length() > 0 ? "\n" : "") + fetchedHierarchyParam.getObject().toString();
                                        hierarchyList.setText(text);
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    paramPanel.add(bSelectHierarchy, BorderLayout.EAST);
                    shownParams.add(cell);
                }
                paramsPanel.add(paramPanel);
            }
        }
        panel.add(paramsPanel, BorderLayout.SOUTH);
    }

    public Integer getObjectId() {
        return objectId;
    }

    private void parseLayout(Node currentNode, final FetchedObject inv, JPanel currentContainer, boolean allowControls) {
        if (currentNode.getNodeName().equals("parameter")) {
            final String cell = currentNode.getAttributes().getNamedItem("name").getNodeValue();
            if (inv.getParameter(cell) instanceof FetchedParam) {
                FetchedParam fetchedParam = inv.getParameter(cell);
                JPanel paramPanel = new JPanel(new BorderLayout(5, 5));
                boolean need2Fill = ((currentNode.getAttributes().getNamedItem("filled") != null) && (currentNode.getAttributes().getNamedItem("filled").getNodeValue().equals("true")));
                if (fetchedParam.getParamType().getName().equalsIgnoreCase("������") || fetchedParam.getParamType().getName().equalsIgnoreCase("�����") || fetchedParam.getParamType().getName().equalsIgnoreCase("������������ �����") || fetchedParam.getParamType().getName().equalsIgnoreCase("����� �����")) {
                    if (!fetchedParam.getParamType().getName().equalsIgnoreCase("�����")) paramPanel.add(new JLabel(fetchedParam.getName() + (need2Fill ? " *" : "")), BorderLayout.WEST);
                    JTextComponent field = (fetchedParam.getParamType().getName().equalsIgnoreCase("�����") ? new JTextArea(((fetchedParam.getValue() != null && (!DatabaseObject.isUnassigned(fetchedParam.getValue()))) ? fetchedParam.getValue().toString() : "")) : new JTextField(((fetchedParam.getValue() != null) && (!DatabaseObject.isUnassigned(fetchedParam.getValue())) ? fetchedParam.getValue().toString() : ""), 20));
                    if (field instanceof JTextArea) {
                        JScrollPane sp = new JScrollPane(field);
                        sp.setBorder(new TitledBorder(cell));
                        paramPanel.add(sp, BorderLayout.CENTER);
                    } else paramPanel.add(field, BorderLayout.CENTER);
                    if (!allowControls) field.setEditable(false);
                    paramValues.put(cell, field);
                    if (need2Fill) need2BeFilled.put(cell, field);
                    shownParams.add(cell);
                }
                if (fetchedParam.getParamType().getName().equalsIgnoreCase("����")) {
                }
                if (fetchedParam.getParamType().getName().equalsIgnoreCase("��������")) {
                    paramPanel.add(new JLabel(cell + (need2Fill ? " *" : "")), BorderLayout.WEST);
                    final FetchedHierarchyParam fetchedHParam = (FetchedHierarchyParam) fetchedParam.getValue();
                    final JTextArea hierarchyList = new JTextArea(1, 15);
                    hierarchyList.setEditable(false);
                    if (((FetchedHierarchyParam) fetchedParam.getValue()).getObject() != null) {
                        DBObjectManager om = new DBObjectManager();
                        try {
                            String text = "";
                            ArrayList<FetchedHierarchyParam> fetchedHierarchyParamArrayList = om.getHierarchy(fetchedHParam);
                            for (FetchedHierarchyParam fetchedHierarchyParam : fetchedHierarchyParamArrayList) text += (text.length() > 0 ? "\n" : "") + fetchedHierarchyParam.getObject().toString();
                            hierarchyList.setText(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (need2Fill) need2BeFilled.put(cell, hierarchyList);
                    paramPanel.add(hierarchyList, BorderLayout.CENTER);
                    JButton bSelectHierarchy = new JButton("...");
                    if (!allowControls) bSelectHierarchy.setEnabled(false);
                    bSelectHierarchy.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            final HierarchySelecter selecter = new HierarchySelecter(fetchedHParam.getHierarchyId());
                            MainView.addFrame("������ �� ��������", selecter).addInternalFrameListener(new InternalFrameAdapter() {

                                public void internalFrameClosed(InternalFrameEvent e) {
                                    if (selecter.getValue() == null) return;
                                    FetchedHierarchyParam param = new FetchedHierarchyParam();
                                    param.setHierarchyId(selecter.getValue().getHierarchy());
                                    param.setId(selecter.getValue().getId());
                                    inv.getParameter(cell).setValue(param);
                                    DBObjectManager om = new DBObjectManager();
                                    try {
                                        String text = "";
                                        ArrayList<FetchedHierarchyParam> fetchedHierarchyParamArrayList = om.getHierarchy(param);
                                        for (FetchedHierarchyParam fetchedHierarchyParam : fetchedHierarchyParamArrayList) text += (text.length() > 0 ? "\n" : "") + fetchedHierarchyParam.getObject().toString();
                                        hierarchyList.setText(text);
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    paramPanel.add(bSelectHierarchy, BorderLayout.EAST);
                    shownParams.add(cell);
                }
                if (currentNode.getAttributes().getNamedItem("position") != null) {
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("center")) currentContainer.add(paramPanel, BorderLayout.CENTER);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("east")) currentContainer.add(paramPanel, BorderLayout.EAST);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("west")) currentContainer.add(paramPanel, BorderLayout.WEST);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("north")) currentContainer.add(paramPanel, BorderLayout.NORTH);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("south")) currentContainer.add(paramPanel, BorderLayout.SOUTH);
                } else currentContainer.add(paramPanel);
            }
        } else {
            if (currentNode.getNodeName().equals("block")) {
                JPanel panel = new JPanel();
                if (currentNode.getAttributes().getNamedItem("layout") != null) {
                    LayoutManager lm = null;
                    if (currentNode.getAttributes().getNamedItem("layout").getNodeValue().equals("vertical")) lm = new BoxLayout(panel, BoxLayout.Y_AXIS);
                    if (currentNode.getAttributes().getNamedItem("layout").getNodeValue().equals("horizontal")) lm = new BoxLayout(panel, BoxLayout.X_AXIS);
                    if (currentNode.getAttributes().getNamedItem("layout").getNodeValue().equals("border")) lm = new BorderLayout(5, 5);
                    panel.setLayout(lm);
                } else {
                    BoxLayout boxL = new BoxLayout(panel, BoxLayout.Y_AXIS);
                    panel.setLayout(boxL);
                }
                if (currentNode.getAttributes().getNamedItem("position") != null) {
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("center")) currentContainer.add(panel, BorderLayout.CENTER);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("east")) currentContainer.add(panel, BorderLayout.EAST);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("west")) currentContainer.add(panel, BorderLayout.WEST);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("north")) currentContainer.add(panel, BorderLayout.NORTH);
                    if (currentNode.getAttributes().getNamedItem("position").getNodeValue().equals("south")) currentContainer.add(panel, BorderLayout.SOUTH);
                } else currentContainer.add(panel);
                for (int nI = 0; nI < currentNode.getChildNodes().getLength(); ++nI) {
                    parseLayout(currentNode.getChildNodes().item(nI), inv, panel, allowControls);
                }
            } else for (int nI = 0; nI < currentNode.getChildNodes().getLength(); ++nI) parseLayout(currentNode.getChildNodes().item(nI), inv, currentContainer, allowControls);
        }
    }

    public String[] getUpdateStrings() {
        return new String[] { "Object:" + objectId };
    }

    public void update(UpdateTypes updateType, FetchedObject object, HashMap<String, Object> additionalParams) {
        if (updateType.equals(UpdateTypes.DELETED)) closeParent(this);
    }
}
