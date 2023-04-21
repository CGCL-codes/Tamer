package com.coremedia.iso.gui;

import com.googlecode.mp4parser.AbstractBox;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.FullBox;
import com.coremedia.iso.boxes.UnknownBox;
import com.coremedia.iso.gui.transferhelper.StringTransferValue;
import com.coremedia.iso.gui.transferhelper.TransferHelperFactory;
import com.coremedia.iso.gui.transferhelper.TransferValue;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Detailed view of a Box.
 */
public class GenericBoxPane extends JPanel {

    private Box box;

    GridBagLayout gridBagLayout;

    GridBagConstraints gridBagConstraints;

    static Properties names = new Properties();

    static {
        try {
            names.load(GenericBoxPane.class.getResourceAsStream("/names.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Collection<String> skipList = Arrays.asList("class", "boxes", "deadBytes", "type", "userType", "size", "displayName", "contentSize", "header", "version", "flags", "isoFile", "parent", "data", "omaDrmData", "content", "tracks", "sampleSizeAtIndex", "numOfBytesToFirstChild");

    public GenericBoxPane(Box box) {
        this.box = box;
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(3, 3, 0, 0);
        this.setLayout(gridBagLayout);
        addHeader();
        addProperties();
    }

    private void add(String name, JComponent view) {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = .01;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel(name);
        gridBagLayout.setConstraints(nameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagLayout.setConstraints(view, gridBagConstraints);
        this.add(nameLabel);
        this.add(view);
        gridBagConstraints.gridy++;
    }

    protected void addHeader() {
        JLabel displayName = new JLabel();
        if (box instanceof UnknownBox) {
            displayName.setText("Unknown Box - " + box.getType());
        } else {
            displayName.setText(names.getProperty(box.getType(), box.getType()));
        }
        Font curFont = displayName.getFont();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        displayName.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 20));
        gridBagLayout.setConstraints(displayName, gridBagConstraints);
        this.add(displayName);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridy++;
        add("type", new NonEditableJTextField(box.getType()));
        add("size", new NonEditableJTextField(String.valueOf(box.getSize())));
        if (box instanceof FullBox) {
            FullBox fullBox = (FullBox) box;
            add("version", new NonEditableJTextField(String.valueOf(fullBox.getVersion())));
            add("flags", new NonEditableJTextField(Integer.toHexString(fullBox.getFlags())));
        }
        gridBagConstraints.gridwidth = 2;
        gridBagLayout.setConstraints(new JSeparator(), gridBagConstraints);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridy++;
    }

    protected void addProperties() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(box.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            boolean editable = false;
            final List<TransferValue> editors = new LinkedList<TransferValue>();
            JButton apply = new JButton("Apply changes");
            apply.setEnabled(false);
            apply.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    for (TransferValue editor : editors) {
                        editor.go();
                    }
                    Container c = GenericBoxPane.this.getParent();
                    while (!(c instanceof IsoViewerPanel)) {
                        c = c.getParent();
                    }
                    ((IsoViewerPanel) c).showDetails(box);
                }
            });
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                if (!skipList.contains(name) && propertyDescriptor.getReadMethod() != null && !AbstractBox.class.isAssignableFrom(propertyDescriptor.getReadMethod().getReturnType())) {
                    final Object value = propertyDescriptor.getReadMethod().invoke(box, (Object[]) null);
                    if (value == null) {
                        add(name, new NonEditableJTextField(""));
                    } else if (Number.class.isAssignableFrom(value.getClass())) {
                        if (propertyDescriptor.getWriteMethod() != null) {
                            JFormattedTextField jftf = new JFormattedTextField(NumberFormat.getNumberInstance());
                            jftf.setValue(value);
                            jftf.getDocument().addDocumentListener(new ActivateOnChange(apply));
                            editors.add(TransferHelperFactory.getNumberTransferHelper(value.getClass(), box, propertyDescriptor.getWriteMethod(), jftf));
                            add(name, jftf);
                            editable = true;
                        } else {
                            add(name, new NonEditableJTextField(value.toString()));
                        }
                    } else if (value.getClass().equals(String.class)) {
                        if (propertyDescriptor.getWriteMethod() != null) {
                            JTextField jtf = new JTextField(value.toString());
                            jtf.getDocument().addDocumentListener(new ActivateOnChange(apply));
                            editors.add(new StringTransferValue(jtf, box, propertyDescriptor.getWriteMethod()));
                            add(name, jtf);
                            editable = true;
                        } else {
                            add(name, new NonEditableJTextField(value.toString()));
                        }
                    } else if (value.getClass().equals(Boolean.class)) {
                        if (propertyDescriptor.getWriteMethod() != null) {
                            JCheckBox jCheckBox = new JCheckBox(value.toString(), null, (Boolean) value);
                            add(name, jCheckBox);
                            editable = false;
                        } else {
                            add(name, new NonEditableJTextField(value.toString()));
                        }
                    } else if (value.getClass().isArray()) {
                        int length = Array.getLength(value);
                        if (value.getClass().getComponentType().isAssignableFrom(String.class)) {
                            JScrollPane jScrollPane = new JScrollPane();
                            jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
                            JList jl = new JList();
                            jl.setCellRenderer(new MultiLineCellRenderer());
                            final int finalLength = length;
                            jl.setModel(new ListModel() {

                                public int getSize() {
                                    return finalLength;
                                }

                                public Object getElementAt(int index) {
                                    return Array.get(value, index);
                                }

                                public void addListDataListener(ListDataListener l) {
                                }

                                public void removeListDataListener(ListDataListener l) {
                                }
                            });
                            jScrollPane.getViewport().add(jl);
                            jScrollPane.setPreferredSize(new Dimension(400, 200));
                            add(name, jScrollPane);
                        } else {
                            if (length < 50) {
                                StringBuffer valueBuffer = new StringBuffer();
                                valueBuffer.append("[");
                                boolean trucated = false;
                                if (length > 1000) {
                                    trucated = true;
                                    length = 1000;
                                }
                                for (int j = 0; j < length; j++) {
                                    if (j > 0) {
                                        valueBuffer.append(", ");
                                    }
                                    Object item = Array.get(value, j);
                                    valueBuffer.append(item != null ? item.toString() : "");
                                }
                                if (trucated) {
                                    valueBuffer.append(", ...");
                                }
                                valueBuffer.append("]");
                                add(name, new NonEditableJTextField(valueBuffer.toString()));
                            } else {
                                JScrollPane jScrollPane = new JScrollPane();
                                jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
                                JList jl = new JList();
                                final int finalLength = length;
                                jl.setModel(new ListModel() {

                                    public int getSize() {
                                        return finalLength;
                                    }

                                    public Object getElementAt(int index) {
                                        return Array.get(value, index);
                                    }

                                    public void addListDataListener(ListDataListener l) {
                                    }

                                    public void removeListDataListener(ListDataListener l) {
                                    }
                                });
                                jScrollPane.getViewport().add(jl);
                                add(name, jScrollPane);
                            }
                        }
                    } else if (List.class.isAssignableFrom(value.getClass())) {
                        JScrollPane jScrollPane = new JScrollPane();
                        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
                        JList jl = new JList();
                        final int finalLength = ((List) value).size();
                        jl.setModel(new ListModel() {

                            public int getSize() {
                                return finalLength;
                            }

                            public Object getElementAt(int index) {
                                return ((List) value).get(index);
                            }

                            public void addListDataListener(ListDataListener l) {
                            }

                            public void removeListDataListener(ListDataListener l) {
                            }
                        });
                        jScrollPane.getViewport().add(jl);
                        add(name + " (" + finalLength + " entries)", jScrollPane);
                    }
                }
            }
            if (editable) {
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy++;
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.EAST;
                gridBagLayout.setConstraints(apply, gridBagConstraints);
                add(apply);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    class ActivateOnChange implements DocumentListener {

        JComponent toBeActivated;

        ActivateOnChange(JComponent toBeActivated) {
            this.toBeActivated = toBeActivated;
        }

        public void insertUpdate(DocumentEvent e) {
            toBeActivated.setEnabled(true);
        }

        public void removeUpdate(DocumentEvent e) {
            toBeActivated.setEnabled(true);
        }

        public void changedUpdate(DocumentEvent e) {
            toBeActivated.setEnabled(true);
        }
    }
}
