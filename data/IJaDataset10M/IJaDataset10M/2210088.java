package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import io.PropertiesXMLLoader;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kmeans.Cluster;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import log.LogAdmin;
import preProcess.GenerateIDF;

public class GenIDFPanel extends JPanel {

    private LogAdmin m_logAdmin;

    private JPanel m_Panel_BasicPro;

    private JLabel m_Label_BasicPro;

    private JTextField m_Field_BasicPro;

    private JPanel m_Panel_GenIDFPara;

    private JLabel m_Label_GenIDFPara;

    private JTextField m_Field_GenIDFPara;

    private JPanel m_Panel_Logger;

    private JLabel m_Label_Logger;

    private JTextField m_Field_Logger;

    private JPanel m_Panel_Info;

    private JTextArea m_TextArea_info;

    private JPanel m_Panel_Comm;

    private JButton m_Button_Cluster;

    private JButton m_Button_ClearText;

    private JButton m_Button_Exit;

    private boolean m_Running = false;

    private Thread m_GenIDFThread;

    public GenIDFPanel() {
        super();
        initPanel();
        this.setSize(800, 600);
        this.setVisible(true);
    }

    public void initPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 1;
        gbc.ipady = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        m_Panel_BasicPro = new JPanel();
        m_Label_BasicPro = new JLabel("��pro��XML�ļ���");
        m_Field_BasicPro = new JTextField(20);
        m_Panel_BasicPro.add(m_Label_BasicPro);
        m_Panel_BasicPro.add(m_Field_BasicPro);
        this.add(m_Panel_BasicPro, gbc);
        gbc.gridy = 1;
        m_Panel_GenIDFPara = new JPanel();
        m_Label_GenIDFPara = new JLabel("���IDF��Ϣ��XML�ļ���");
        m_Field_GenIDFPara = new JTextField(20);
        m_Panel_GenIDFPara.add(m_Label_GenIDFPara);
        m_Panel_GenIDFPara.add(m_Field_GenIDFPara);
        this.add(m_Panel_GenIDFPara, gbc);
        gbc.gridy = 2;
        m_Panel_Logger = new JPanel();
        m_Label_Logger = new JLabel("logger�����ļ���");
        m_Field_Logger = new JTextField(20);
        m_Panel_Logger.add(m_Label_Logger);
        m_Panel_Logger.add(m_Field_Logger);
        this.add(m_Panel_Logger, gbc);
        gbc.gridy = 4;
        gbc.gridheight = 2;
        m_Panel_Info = new JPanel();
        m_TextArea_info = new JTextArea();
        m_TextArea_info.setEditable(false);
        m_TextArea_info.setLineWrap(true);
        m_TextArea_info.setColumns(33);
        m_TextArea_info.setRows(6);
        m_TextArea_info.setText("��Ϣ����:\n");
        JScrollPane scroll = new JScrollPane(m_TextArea_info);
        m_Panel_Info.add(scroll);
        m_Panel_Info.setBackground(Color.LIGHT_GRAY);
        this.add(m_Panel_Info, gbc);
        gbc.gridy = 6;
        m_Panel_Comm = new JPanel();
        m_Button_Cluster = new JButton("����IDF��Ϣ");
        m_Button_Cluster.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                handGenIDF(m_Field_Logger.getText(), m_Field_BasicPro.getText(), m_Field_GenIDFPara.getText());
            }
        });
        m_Button_ClearText = new JButton("�����Ϣ");
        m_Button_ClearText.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                m_TextArea_info.setText("");
            }
        });
        m_Button_Exit = new JButton("�˳�");
        m_Button_Exit.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (m_GenIDFThread != null) {
                    m_GenIDFThread.stop();
                    m_GenIDFThread = null;
                }
                System.exit(0);
            }
        });
        m_Panel_Comm.add(m_Button_Cluster);
        m_Panel_Comm.add(m_Button_ClearText);
        m_Panel_Comm.add(m_Button_Exit);
        this.add(m_Panel_Comm, gbc);
    }

    private void handGenIDF(String logger, String proXML, String genIDFParaXML) {
        try {
            Logger myLogger = Logger.getLogger("myLogger");
            PropertyConfigurator.configure(logger);
            PropertiesXMLLoader pro = new PropertiesXMLLoader(proXML);
            PropertiesXMLLoader para = new PropertiesXMLLoader(genIDFParaXML);
            m_logAdmin = new LogAdmin();
            m_logAdmin.setLogger(myLogger);
            m_logAdmin.setJTextArea(m_TextArea_info);
            m_logAdmin.setButton(m_Button_Cluster);
            if (m_GenIDFThread != null) m_GenIDFThread.stop();
            m_GenIDFThread = new GenerateIDF(m_logAdmin, pro, para);
            m_GenIDFThread.start();
            m_TextArea_info.append("����IDF��Ϣ��ϣ���\n");
        } catch (Exception e) {
            m_logAdmin.printLog("���IDFֵ�쳣");
            m_logAdmin.printLog(e.getMessage());
            m_logAdmin.reEnableButton();
            e.printStackTrace();
        }
    }
}
