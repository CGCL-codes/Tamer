package com.tensegrity.palobrowser.dialogs.format;

import java.util.ArrayList;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.palo.api.Dimension;
import com.tensegrity.palobrowser.PalobrowserPlugin;
import com.tensegrity.palobrowser.cubeview.TrafficLightFormatData;
import com.tensegrity.palobrowser.dialogs.DialogsMessages;
import com.tensegrity.palobrowser.preferences.PreferenceConstants;

public class TrafficLightTab extends AbstractFormatTab {

    private ArrayList<TrafficLightRow> trafficLightRows;

    private boolean isActive;

    private Button addArea;

    private Button removeArea;

    private Group trafficLightAreas;

    private ScrolledComposite trafficLightComposite;

    private boolean modified;

    private static final int MAX_AREAS = 30;

    class TrafficLightRow {

        private Label fromLabel;

        private Text valueFrom;

        private Label toLabel;

        private Text valueTo;

        private Label fontLabel;

        private RGB backgroundColor;

        private Label colorLabel;

        private RGB foregroundColor;

        private FontData[] font;

        private ColorSelector colorSelector;

        private Button fontButton;

        private boolean minIsSet;

        private boolean maxIsSet;

        private double minValue;

        private double maxValue;

        public TrafficLightRow(final Composite parent) {
            fromLabel = new Label(parent, SWT.NONE);
            fromLabel.setText(DialogsMessages.getString("TrafficLightTab.ValueFrom"));
            valueFrom = new Text(parent, SWT.NONE);
            valueFrom.setLayoutData(new GridData(60, SWT.DEFAULT));
            valueFrom.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    minIsSet = valueFrom.getText().trim().length() > 0;
                    if (minIsSet) {
                        try {
                            minValue = Double.parseDouble(valueFrom.getText());
                        } catch (NumberFormatException ex) {
                            minIsSet = false;
                        }
                    }
                }
            });
            toLabel = new Label(parent, SWT.NONE);
            toLabel.setText(DialogsMessages.getString("TrafficLightTab.ValueThrough"));
            valueTo = new Text(parent, SWT.NONE);
            valueTo.setLayoutData(new GridData(60, SWT.DEFAULT));
            valueTo.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    maxIsSet = valueTo.getText().trim().length() > 0;
                    if (maxIsSet) {
                        try {
                            maxValue = Double.parseDouble(valueTo.getText());
                        } catch (NumberFormatException ex) {
                            maxIsSet = false;
                        }
                    }
                }
            });
            colorLabel = new Label(parent, SWT.NONE);
            colorLabel.setText(DialogsMessages.getString("TrafficLightTab.Background"));
            colorSelector = new ColorSelector(parent);
            colorSelector.setColorValue(new RGB(0, 0, 0));
            backgroundColor = new RGB(0, 0, 0);
            colorSelector.addListener(new IPropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent event) {
                    backgroundColor = colorSelector.getColorValue();
                }
            });
            fontLabel = new Label(parent, SWT.NONE);
            fontLabel.setText(DialogsMessages.getString("TrafficLightTab.Font"));
            fontButton = new Button(parent, SWT.None);
            fontButton.setText(DialogsMessages.getString("TrafficLightTab.Change"));
            fontButton.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event event) {
                    FontDialog fd = new FontDialog(parent.getShell());
                    fd.open();
                    fd.getFontList();
                    if (font != null) {
                        foregroundColor = fd.getRGB();
                    }
                }
            });
            font = PreferenceConverter.getFontDataArray(PalobrowserPlugin.getDefault().getPreferenceStore(), PreferenceConstants.PREF_CUBE_FONT_CONTENT);
        }

        void dispose() {
            valueFrom.dispose();
            valueTo.dispose();
            fontButton.dispose();
            colorSelector.getButton().dispose();
            colorLabel.dispose();
            fontLabel.dispose();
            fromLabel.dispose();
            toLabel.dispose();
        }

        void setEnabled(boolean active) {
            colorSelector.setEnabled(active);
            valueFrom.setEnabled(active);
            valueTo.setEnabled(active);
            fontButton.setEnabled(active);
            colorLabel.setEnabled(active);
            fontLabel.setEnabled(active);
            fromLabel.setEnabled(active);
            toLabel.setEnabled(active);
        }

        double getMinValue() {
            return minValue;
        }

        double getMaxValue() {
            return maxValue;
        }

        boolean isMinSet() {
            return minIsSet;
        }

        boolean isMaxSet() {
            return maxIsSet;
        }

        void setForegroundColor(RGB foreground) {
            foregroundColor = foreground;
        }

        void setBackgroundColor(RGB background) {
            backgroundColor = background;
            if (background != null) {
                colorSelector.setColorValue(background);
            }
        }

        RGB getForegroundColor() {
            return foregroundColor;
        }

        RGB getBackgroundColor() {
            return backgroundColor;
        }

        FontData[] getFont() {
            return font;
        }

        void setMinValue(double min) {
            valueFrom.setText("" + min);
        }

        void setMaxValue(double max) {
            valueTo.setText("" + max);
        }

        int getLowestPoint() {
            return fontButton.getLocation().y + fontButton.getSize().y;
        }
    }

    public TrafficLightTab(CTabFolder parent, FormatDialog fd) {
        super(parent, DialogsMessages.getString("TrafficLightTab.Title"), fd);
        modified = false;
    }

    private final void addRow(final Composite parent, boolean setMin, boolean setMax, double min, double max, RGB background, RGB foreground) {
        if (trafficLightRows == null) {
            trafficLightRows = new ArrayList<TrafficLightRow>();
        } else {
            if (trafficLightRows.size() > MAX_AREAS) {
                return;
            }
        }
        TrafficLightRow row = new TrafficLightRow(parent);
        if (setMin) {
            row.setMinValue(min);
        }
        if (setMax) {
            row.setMaxValue(max);
        }
        row.setBackgroundColor(background);
        row.setForegroundColor(foreground);
        trafficLightRows.add(row);
    }

    private final void removeRow() {
        if (trafficLightRows.size() > 1) {
            int index = trafficLightRows.size() - 1;
            trafficLightRows.get(index).dispose();
            trafficLightRows.remove(index);
        }
    }

    protected void createFormatContent(final Composite parent) {
        GridLayout layout = new GridLayout(1, false);
        parent.setLayout(layout);
        Group group = new Group(parent, SWT.NONE);
        group.setText(DialogsMessages.getString("TrafficLightTab.GeneralGroup"));
        GridLayout layout2 = new GridLayout(3, false);
        group.setLayout(layout2);
        final Button activateButton = new Button(group, SWT.CHECK);
        activateButton.setText(DialogsMessages.getString("TrafficLightTab.Enable"));
        activateButton.addSelectionListener(new SelectionListener() {

            private final void toggleState() {
                isActive = activateButton.getSelection();
                addArea.setEnabled(isActive);
                removeArea.setEnabled(isActive);
                for (TrafficLightRow row : trafficLightRows) {
                    row.setEnabled(isActive);
                }
                modified = true;
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                toggleState();
            }

            public void widgetSelected(SelectionEvent e) {
                toggleState();
            }
        });
        addArea = new Button(group, SWT.NONE);
        addArea.setText(DialogsMessages.getString("TrafficLightTab.Add"));
        addArea.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                int size = trafficLightRows.size();
                boolean setMin = false;
                boolean setMax = false;
                double min = 0;
                double max = 0;
                RGB background = null;
                if (size == 1) {
                    setMin = true;
                    min = 0.0;
                    setMax = true;
                    max = 1000.0;
                    background = new RGB(255, 255, 0);
                } else if (size == 2) {
                    background = new RGB(0, 255, 0);
                }
                addRow(trafficLightAreas, setMin, setMax, min, max, background, null);
                removeArea.setEnabled(size > 1);
                addArea.setEnabled(size < MAX_AREAS);
                trafficLightAreas.layout(true);
                trafficLightAreas.setSize(500, Math.max(200, trafficLightRows.get(size).getLowestPoint()));
                trafficLightAreas.layout(true);
            }
        });
        removeArea = new Button(group, SWT.NONE);
        removeArea.setText(DialogsMessages.getString("TrafficLightTab.Remove"));
        removeArea.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                removeRow();
                int size = trafficLightRows.size();
                removeArea.setEnabled(size > 1);
                addArea.setEnabled(size < MAX_AREAS);
                trafficLightAreas.setSize(500, Math.max(200, trafficLightRows.get(size - 1).getLowestPoint()));
                trafficLightAreas.layout(true);
            }
        });
        trafficLightComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
        trafficLightComposite.setLayout(new GridLayout(1, false));
        trafficLightComposite.setLayoutData(new GridData(SWT.DEFAULT, 200));
        trafficLightAreas = new Group(trafficLightComposite, SWT.V_SCROLL);
        trafficLightAreas.setText(DialogsMessages.getString("TrafficLightTab.TrafficLightRangeGroup"));
        GridLayout layout3 = new GridLayout(8, false);
        trafficLightAreas.setLayout(layout3);
        trafficLightAreas.setLayoutData(new GridData(SWT.DEFAULT, 150));
        addRow(trafficLightAreas, false, false, 0, 0, new RGB(255, 0, 0), null);
        addRow(trafficLightAreas, true, true, 0, 1000, new RGB(255, 255, 0), null);
        addRow(trafficLightAreas, false, false, 0, 0, new RGB(0, 255, 0), null);
        trafficLightAreas.setSize(500, 200);
        trafficLightComposite.setContent(trafficLightAreas);
        addArea.setEnabled(false);
        removeArea.setEnabled(false);
        for (TrafficLightRow row : trafficLightRows) {
            row.setEnabled(false);
        }
    }

    protected void processInput(Dimension dimension) {
    }

    public void widgetDisposed(DisposeEvent e) {
    }

    public TrafficLightFormatData getResult() {
        if (!isActive) {
            if (!modified) {
                return null;
            } else {
                return new TrafficLightFormatData(new double[0], new double[0], new RGB[0], new RGB[0], new FontData[0][0]);
            }
        }
        TrafficLightFormatData result;
        int n = trafficLightRows.size();
        double[] minVals = new double[n];
        double[] maxVals = new double[n];
        RGB[] foreground = new RGB[n];
        RGB[] background = new RGB[n];
        FontData[][] fonts = new FontData[n][];
        int counter = 0;
        for (TrafficLightRow r : trafficLightRows) {
            minVals[counter] = r.isMinSet() ? r.getMinValue() : Double.NaN;
            maxVals[counter] = r.isMaxSet() ? r.getMaxValue() : Double.NaN;
            foreground[counter] = r.getForegroundColor();
            background[counter] = r.getBackgroundColor();
            fonts[counter] = r.getFont();
            counter++;
        }
        result = new TrafficLightFormatData(minVals, maxVals, background, foreground, fonts);
        return result;
    }
}
