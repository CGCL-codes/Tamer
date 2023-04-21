package br.com.visualmidia.ui.dialog;

import java.text.SimpleDateFormat;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import br.com.visualmidia.GD;
import br.com.visualmidia.business.GDDate;
import br.com.visualmidia.business.Registration;
import br.com.visualmidia.business.RegistrationMap;
import br.com.visualmidia.persistence.GetRegistration;
import br.com.visualmidia.system.GDSystem;
import br.com.visualmidia.ui.widgets.DateText;

/**
 * @author  Lucas
 */
public class UpdateReplacementDialog extends TitleAreaDialog {

    private Composite dialog;

    private Registration registration;

    private GDDate date;

    private Label dateLabel;

    private DateText dateText;

    private int startWorkTime;

    private int endWorkTime;

    private Combo hourCombo;

    private RegistrationMap registrationMap;

    private boolean isReplacement;

    private Combo weekDayCombo;

    public UpdateReplacementDialog(Shell parent, GDDate date, String idRegistration, boolean isReplacement) {
        super(parent);
        this.date = date;
        GDSystem system = GDSystem.getInstance();
        this.registrationMap = system.getRegistratioMap();
        GD gd = GD.getInstance();
        this.startWorkTime = gd.getWorktime("start");
        this.endWorkTime = gd.getWorktime("end");
        this.isReplacement = isReplacement;
        try {
            this.registration = (Registration) system.query(new GetRegistration(idRegistration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Control createContents(Composite arg0) {
        Control control = super.createContents(arg0);
        setTitle("Mover agendamento do dia " + new GDDate(date).getFormatedDate());
        setMessage("Digite a nova data e hor�ria do agendamento.");
        return control;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        dialog = new Composite(composite, SWT.NONE);
        dialog.setLayout(new FormLayout());
        createDateLabel();
        createDateText();
        createWeekDayCombo();
        createHourLabel();
        createHourCombo();
        Label separator = new Label(dialog, SWT.SEPARATOR | SWT.HORIZONTAL);
        FormData data = new FormData();
        data.top = new FormAttachment(hourCombo, 50);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        separator.setLayoutData(data);
        return parent;
    }

    private void createHourCombo() {
        hourCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
        int index = 0;
        for (int i = startWorkTime; i < endWorkTime; i++) {
            hourCombo.add(i + ":00");
            if (i == date.getHourOfDay()) {
                index = i;
            }
        }
        hourCombo.select(index - startWorkTime);
        FormData data = new FormData();
        data.top = new FormAttachment(dateLabel, 10);
        data.left = new FormAttachment(dateLabel, 10);
        hourCombo.setLayoutData(data);
    }

    private void createWeekDayCombo() {
        weekDayCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
        if (isReplacement) {
            weekDayCombo.setVisible(false);
        }
        weekDayCombo.add("Domingo");
        weekDayCombo.add("Segunda-feira");
        weekDayCombo.add("Ter�a-feira");
        weekDayCombo.add("Quarta-feira");
        weekDayCombo.add("Quinta-feira");
        weekDayCombo.add("Sexta-feira");
        weekDayCombo.add("Sabado");
        weekDayCombo.select(0);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(dateLabel, 10);
        data.right = new FormAttachment(100, -330);
        weekDayCombo.setLayoutData(data);
    }

    private void createDateText() {
        dateText = new DateText(dialog, SWT.BORDER | SWT.READ_ONLY);
        dateText.setValue(new GDDate(date).getDate());
        if (!isReplacement) {
            dateText.setVisible(false);
        }
        FormData data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(dateLabel, 10);
        data.right = new FormAttachment(100, -330);
        dateText.setLayoutData(data);
    }

    private void createDateLabel() {
        dateLabel = new Label(dialog, SWT.NONE);
        dateLabel.setText("Data: ");
        FormData data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(0, 10);
        dateLabel.setLayoutData(data);
    }

    private void createHourLabel() {
        Label hourLabel = new Label(dialog, SWT.NONE);
        hourLabel.setText("Hora: ");
        FormData data = new FormData();
        data.top = new FormAttachment(dateLabel, 10);
        data.left = new FormAttachment(0, 10);
        hourLabel.setLayoutData(data);
    }

    @Override
    protected void buttonPressed(int event) {
        if (event == OK) {
            String newHour = hourCombo.getText().split(":")[0];
            GDDate newGDDate = new GDDate(dateText.getText());
            if (!isReplacement) {
                GDDate today = new GDDate();
                int weekDay = weekDayCombo.getSelectionIndex();
                while (true) {
                    if (today.getWeekDay() == weekDay) break;
                    today.addDays(1);
                }
                newGDDate = new GDDate(today);
            }
            newGDDate.setHourOfDay(Integer.parseInt(newHour));
            newGDDate.setMinuteOfHour(0);
            newGDDate.setSecondOfMinute(0);
            String weekDay = new SimpleDateFormat("EEEEE").format(newGDDate.getDate()).replace("-feira", "");
            int numberOfVacancy = registrationMap.getMaxVacancy(weekDay, newHour);
            int numberOfReplacementOfTheDay = registrationMap.getNumberOfReplacementOfTheDay(newGDDate);
            int numberOfAppointmentsOfTheDay = registrationMap.getNumberOfAppointmentsOfTheDay(newGDDate);
            int numberOfFreeVacancy = numberOfVacancy - numberOfAppointmentsOfTheDay - numberOfReplacementOfTheDay;
            if (numberOfFreeVacancy > 0 && newGDDate.after(new GDDate())) {
                if (!registrationMap.isPersonHasAppointmentsOrReplacementsOnThatDate(newGDDate, registration.getStudent())) {
                    registrationMap.moveRegistrationAppointment(registration, date, newGDDate);
                    close();
                } else {
                    setErrorMessage("Aluno j� agendou aula neste hor�rio. Selecione outra data ou hor�rio.");
                }
            } else {
                setErrorMessage("Data ou hor�rio indispon�vel. Selecione outra data ou hor�rio.");
            }
        } else {
            close();
        }
        setReturnCode(event);
    }
}
