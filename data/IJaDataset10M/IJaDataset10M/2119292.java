package org.km.xplane.airports.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.km.xplane.airports.AirportPlugin;
import org.km.xplane.airports.gui.IElementSettingsPage;
import org.km.xplane.airports.gui.internal.data.AirportPartAdapter;
import org.km.xplane.airports.gui.internal.data.RampAdapter;
import org.km.xplane.airports.math.Position;
import org.km.xplane.airports.math.Tools;
import org.km.xplane.airports.model.Airport;
import org.km.xplane.airports.model.Ramp;

/**
 * @author km
 *
 */
public class RampEditWidget extends SettingsPage implements IElementSettingsPage {

    private Combo _comboAirport;

    private RampAdapter _rampAdapter;

    private Airport _selectedAirport;

    private int _cols;

    private Text _textName;

    private Text _heading;

    private PositionWidget _position;

    /**
	 * @param parent
	 * @param receiver
	 * @param style
	 */
    public RampEditWidget(Composite parent, IMessageReceiver receiver, int cols, int style) {
        super(parent, receiver, style);
        _cols = cols;
        createContents();
    }

    /**
	 * @param parent
	 */
    private void createContents() {
        this.setLayout(new GridLayout(_cols, false));
        new Label(this, SWT.NULL).setText("Airport:");
        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        _comboAirport = new Combo(this, SWT.READ_ONLY);
        _comboAirport.setLayoutData(data);
        Airport[] aps = AirportPlugin.getDefault().getData().getAirports();
        for (int i = 0; i < aps.length; i++) {
            _comboAirport.add(aps[i].getIDCode() + " - " + aps[i].getName());
            _comboAirport.setData(Integer.toString(i), aps[i]);
        }
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        new Label(this, SWT.NONE).setText("Name:");
        _textName = new Text(this, SWT.BORDER);
        _textName.setLayoutData(data);
        _textName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkValues();
            }
        });
        new Label(this, SWT.NONE).setText("Heading:");
        _heading = new Text(this, SWT.BORDER);
        _heading.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkValues();
            }
        });
        _position = new PositionWidget(this, _cols, SWT.NONE);
        _position.setText("Position");
        data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = SWT.FILL;
        data.horizontalSpan = _cols;
        _position.setLayoutData(data);
        _position.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkValues();
            }
        });
    }

    /**
	 * 
	 */
    protected void checkValues() {
        if (_textName.getText().length() == 0) {
            setMessage("Name must be filled.", IMessageReceiver.ERROR);
            setFinished(false);
            return;
        }
        String heading = this._heading.getText();
        try {
            double dblHeading = Double.parseDouble(heading);
            if (dblHeading < 0.0 || dblHeading > 360) {
                setMessage("Heading out of range!", IMessageReceiver.ERROR);
                setFinished(false);
                return;
            }
        } catch (Exception e) {
            setMessage("Invalid Heading format!", IMessageReceiver.ERROR);
            setFinished(false);
            return;
        }
        if (_position.getPosition() == null) {
            setMessage("Position is not valid!", IMessageReceiver.ERROR);
            setFinished(false);
            return;
        }
        setMessage("", IMessageReceiver.NONE);
        setFinished(true);
    }

    public void applyValues() {
        if (_rampAdapter != null) {
            setRempValues((Ramp) _rampAdapter.getAirportPart());
        }
    }

    /**
	 * @param ramp
	 */
    public void setRempValues(Ramp ramp) {
        ramp.setName(_textName.getText());
        ramp.setHeading(_heading.getText());
        ramp.setPosition(_position.getPosition());
    }

    public void setAirportPartAdapter(AirportPartAdapter adapter) {
        _rampAdapter = (RampAdapter) adapter;
        _selectedAirport = _rampAdapter.getAirport();
        initValues();
        checkValues();
    }

    /**
	 * 
	 */
    private void initValues() {
        if (_selectedAirport != null) {
            int selection = 0;
            _comboAirport.removeAll();
            Airport[] aps = AirportPlugin.getDefault().getData().getAirports();
            for (int i = 0; i < aps.length; i++) {
                _comboAirport.add(aps[i].getIDCode() + " - " + aps[i].getName());
                _comboAirport.setData(Integer.toString(i), aps[i]);
                if (aps[i] == _selectedAirport) selection = i;
            }
            _comboAirport.select(selection);
        } else _comboAirport.select(0);
        if (_rampAdapter != null) {
            Ramp ramp = (Ramp) _rampAdapter.getAirportPart();
            _textName.setText(ramp.getName());
            _heading.setText(ramp.getHeading());
            _position.setPosition(ramp.getPosition());
        }
    }

    /**
	 * @param selectedAirport
	 */
    public void setSelectedAirport(Airport selectedAirport) {
        _selectedAirport = selectedAirport;
        initValues();
        checkValues();
    }

    /**
	 * @param position
	 */
    public void setPosition(Position position) {
        _position.setPosition(position);
    }

    /**
	 * @param heading
	 */
    public void setHeading(double heading) {
        _heading.setText(Tools.doubleToString(heading, 1));
    }
}
