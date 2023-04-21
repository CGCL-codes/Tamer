package com.study.pepper.client.ui.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import com.study.jslib.json.JSONArray;
import com.study.jslib.json.JSONObject;
import com.study.pepper.client.ui.basic.AbstractCallback;
import com.study.pepper.client.ui.basic.BasicCard;
import com.study.pepper.client.ui.basic.CinemaComboBoxModel;
import com.study.pepper.client.ui.basic.FormComboBox;
import com.study.pepper.client.ui.basic.FormDateField;
import com.study.pepper.client.ui.basic.FormField;
import com.study.pepper.client.ui.basic.FormFieldType;
import com.study.pepper.client.ui.basic.FormTextField;
import com.study.pepper.client.ui.hall.HallList;
import com.study.pepper.client.ui.image.ImageList;
import com.study.pepper.client.utils.ServerManager;

public class SessionCard extends BasicCard {

    private JLabel lblDate;

    private FormDateField dfDate;

    private JLabel lblFilm;

    private FormComboBox cbFilm;

    private JLabel lblCinema;

    private FormComboBox cbCinema;

    private JLabel lblHall;

    private FormComboBox cbHall;

    private boolean edit = true;

    public SessionCard(String title, JSONObject data, JDesktopPane desktopParent, AbstractCallback callback) {
        super(title, data, desktopParent, callback);
    }

    @Override
    protected void initFields() {
        lblDate = new JLabel("Дата :");
        try {
            dfDate = new FormDateField("date", FormFieldType.STRING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        lblFilm = new JLabel("Название фильма :");
        cbFilm = new FormComboBox("name", FormFieldType.STRING, new JSONArray(ServerManager.getInstance().getFilmsList()));
        lblCinema = new JLabel("Кинотеатр :");
        cbCinema = new FormComboBox("name", FormFieldType.STRING, new JSONArray(ServerManager.getInstance().getCinemasList()));
        lblHall = new JLabel("Залы :");
        cbHall = new FormComboBox("name", FormFieldType.STRING, null);
        setFormFields(new FormField[] { dfDate, cbFilm, cbCinema });
    }

    @Override
    protected void initAfterLoad() {
        cbCinema.setSelectedIndex(0);
        cbHall.setList(new JSONArray(ServerManager.getInstance().getHallsList(((CinemaComboBoxModel) cbCinema.getModel()).getElementIdAt(0))));
        cbHall.setModel(new CinemaComboBoxModel(cbHall.getList()));
        cbCinema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                cbHall.setList(new JSONArray(ServerManager.getInstance().getHallsList(((CinemaComboBoxModel) cbCinema.getModel()).getSelectedItemId())));
                cbHall.setModel(new CinemaComboBoxModel(cbHall.getList()));
            }
        });
    }

    @Override
    protected JPanel createFormPanel() {
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        JPanel pMain = new JPanel();
        pMain.setLayout(null);
        TitledBorder titled = BorderFactory.createTitledBorder(loweredetched, "Фильм");
        titled.setTitleJustification(TitledBorder.LEFT);
        pMain.setBorder(titled);
        pMain.setBounds(leftOffset, topOffset, getWidth() - 30, getHeight() - 100);
        lblDate.setBounds(leftOffset, topOffset, 300, tfHeight);
        pMain.add(lblDate);
        dfDate.setBounds(lblDate.getX(), lblDate.getY() + lblDate.getHeight() + lblOffset, lblDate.getWidth(), tfHeight);
        pMain.add(dfDate);
        lblFilm.setBounds(lblDate.getX(), lblDate.getY() + lblDate.getHeight() + 6 * lblOffset, 300, tfHeight);
        pMain.add(lblFilm);
        cbFilm.setBounds(lblFilm.getX(), lblFilm.getY() + lblFilm.getHeight() + lblOffset, lblFilm.getWidth(), tfHeight);
        pMain.add(cbFilm);
        lblCinema.setBounds(lblFilm.getX(), lblFilm.getY() + lblFilm.getHeight() + 6 * lblOffset, 300, tfHeight);
        pMain.add(lblCinema);
        cbCinema.setBounds(lblCinema.getX(), lblCinema.getY() + lblCinema.getHeight() + lblOffset, lblCinema.getWidth(), tfHeight);
        pMain.add(cbCinema);
        lblHall.setBounds(lblCinema.getX(), lblCinema.getY() + lblCinema.getHeight() + 6 * lblOffset, 300, tfHeight);
        pMain.add(lblHall);
        cbHall.setBounds(lblHall.getX(), lblHall.getY() + lblHall.getHeight() + lblOffset, lblHall.getWidth(), tfHeight);
        pMain.add(cbHall);
        return pMain;
    }

    @Override
    protected void update(JSONObject data) {
        data.put("tohall", ((CinemaComboBoxModel) ((FormComboBox) cbHall).getModel()).getSelectedItem());
        data.put("tohallid", ((CinemaComboBoxModel) ((FormComboBox) cbHall).getModel()).getSelectedItemId());
        data.put("tofilm", ((CinemaComboBoxModel) ((FormComboBox) cbFilm).getModel()).getSelectedItemId());
        data.put("tocinema", ((CinemaComboBoxModel) ((FormComboBox) cbCinema).getModel()).getSelectedItemId());
        String res = ServerManager.getInstance().updateSession(data);
        System.out.println();
    }
}
