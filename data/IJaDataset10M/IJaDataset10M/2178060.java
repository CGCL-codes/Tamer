package net.narusas.daumaccess.ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.JProgressBar;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JButton addCafeButton = null;

	private JButton addBoardButton = null;

	private JButton addWritingButton = null;

	private JPanel jPanel3 = null;

	private JScrollPane jScrollPane = null;

	private JList workList = null;

	private JPanel jPanel4 = null;

	private JScrollPane jScrollPane1 = null;

	private JList accountList = null;

	private JPanel jPanel6 = null;

	private JScrollPane jScrollPane2 = null;

	private JList proxyList = null;

	private JButton removeTaskButton = null;

	private JPanel jPanel7 = null;

	private JScrollPane jScrollPane3 = null;

	private JTextArea logTextArea = null;

	private JToolBar jJToolBarBar = null;

	private JButton goAndStopButton = null;

	private JButton refreshProxyButton = null;

	private JPanel jPanel5 = null;

	private JLabel jLabel1 = null;

	private JSlider 작업간격Slider = null;

	private JButton addSearchButton = null;

	private JPanel jPanel8 = null;

	private JProgressBar jProgressBar = null;

	public TitledBorder taskBorder;

	public TitledBorder accountBorder;

	public TitledBorder proxyBorder;

	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(741, 582);
		this.setResizable(false);
		this.setContentPane(getJContentPane());
		this.setTitle("Daum Cafe Accessor v 1.0");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
			jContentPane.add(getJPanel1(), BorderLayout.CENTER);
			jContentPane.add(getJPanel2(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJJToolBarBar(), gridBagConstraints14);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 2;
			gridBagConstraints16.gridwidth = 1;
			gridBagConstraints16.fill = GridBagConstraints.BOTH;
			gridBagConstraints16.gridy = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.fill = GridBagConstraints.BOTH;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.weightx = 0.8D;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.weightx = 0.7D;
			gridBagConstraints3.weighty = 1.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.weightx = 0.2D;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getJPanel3(), gridBagConstraints1);
			jPanel1.add(getJPanel4(), gridBagConstraints3);
			jPanel1.add(getJPanel6(), gridBagConstraints5);
			jPanel1.add(getJPanel5(), gridBagConstraints15);
			jPanel1.add(getJPanel8(), gridBagConstraints16);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			TitledBorder titledBorder6 = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ub85c\uadf8", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\uad74\ub9bc", Font.BOLD, 11), new Color(51, 51, 51));
			titledBorder6.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.weightx = 1.0;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.setPreferredSize(new Dimension(13, 130));
			jPanel2.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel2.setBorder(titledBorder6);
			jPanel2.add(getJScrollPane3(), gridBagConstraints13);
		}
		return jPanel2;
	}

	/**
	 * This method initializes addCafeButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getAddCafeButton() {
		if (addCafeButton == null) {
			addCafeButton = new JButton();
			addCafeButton.setText("카페추가");
			addCafeButton.setHorizontalAlignment(SwingConstants.CENTER);
			addCafeButton.setEnabled(false);
			addCafeButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return addCafeButton;
	}

	/**
	 * This method initializes addBoardButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getAddBoardButton() {
		if (addBoardButton == null) {
			addBoardButton = new JButton();
			addBoardButton.setText("게시판 추가");
			addBoardButton.setHorizontalAlignment(SwingConstants.CENTER);
			addBoardButton.setEnabled(false);
			addBoardButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return addBoardButton;
	}

	/**
	 * This method initializes addWritingButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getAddWritingButton() {
		if (addWritingButton == null) {
			addWritingButton = new JButton();
			addWritingButton.setText("게시물 추가");
			addWritingButton.setHorizontalAlignment(SwingConstants.CENTER);
			addWritingButton.setEnabled(false);
			addWritingButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return addWritingButton;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getJPanel3() {
		if (jPanel3 == null) {
			TitledBorder titledBorder = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.PLAIN, 11), new Color(51, 51, 51));
			titledBorder.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 1;
			gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 3;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = -1;
			gridBagConstraints51.weightx = 1.0D;
			gridBagConstraints51.gridy = -1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.weightx = 1.0;
			jPanel3 = new JPanel();
			jPanel3.setLayout(new GridBagLayout());
			jPanel3.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel3.setBorder(titledBorder);
			taskBorder = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.PLAIN, 11), new Color(51, 51, 51));
			jPanel3.add(getJScrollPane(), gridBagConstraints);
			jPanel3.add(getJPanel7(), gridBagConstraints61);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTaskList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes workList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getTaskList() {
		if (workList == null) {
			workList = new JList();
			workList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			workList.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return workList;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	JPanel getJPanel4() {
		if (jPanel4 == null) {
			accountBorder = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ub85c\uadf8", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\uad74\ub9bc", Font.BOLD, 11), new Color(51, 51, 51));
			accountBorder.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			accountBorder.setTitle("계정 목록");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			jPanel4 = new JPanel();
			jPanel4.setLayout(new GridBagLayout());
			jPanel4.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel4.setBorder(accountBorder);
			jPanel4.add(getJScrollPane1(), gridBagConstraints2);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getAccountList());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes accountList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getAccountList() {
		if (accountList == null) {
			accountList = new JList();
			accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			accountList.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return accountList;
	}

	/**
	 * This method initializes jPanel6
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getJPanel6() {
		if (jPanel6 == null) {
			proxyBorder = BorderFactory.createTitledBorder(null, "Proxy \ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 11), new Color(51, 51, 51));
			proxyBorder.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			jPanel6 = new JPanel();
			jPanel6.setLayout(new GridBagLayout());
			jPanel6.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel6.setBorder(proxyBorder);
			jPanel6.add(getJScrollPane2(), gridBagConstraints4);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jScrollPane2
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane2.setViewportView(getProxyList());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes proxyList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getProxyList() {
		if (proxyList == null) {
			proxyList = new JList();
			proxyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			proxyList.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return proxyList;
	}

	/**
	 * This method initializes removeTaskButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getRemoveTaskButton() {
		if (removeTaskButton == null) {
			removeTaskButton = new JButton();
			removeTaskButton.setText("전체 작업 제거");
			removeTaskButton.setEnabled(false);
			removeTaskButton.setFont(new Font("Dialog", Font.PLAIN, 11));
			removeTaskButton.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return removeTaskButton;
	}

	/**
	 * This method initializes jPanel7
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			TitledBorder titledBorder3 = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ub85c\uadf8", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 11), new Color(51, 51, 51));
			titledBorder3.setTitle("제어");
			titledBorder3.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints20.gridy = 2;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints19.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.CENTER;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0D;
			gridBagConstraints10.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = -1;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = -1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = -1;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = -1;
			jPanel7 = new JPanel();
			jPanel7.setLayout(new GridBagLayout());
			jPanel7.setFont(new Font("\uad74\ub9bc", Font.PLAIN, 11));
			jPanel7.setBorder(titledBorder3);
			jPanel7.add(getAddCafeButton(), gridBagConstraints8);
			jPanel7.add(getAddBoardButton(), gridBagConstraints9);
			jPanel7.add(getAddWritingButton(), gridBagConstraints12);
			jPanel7.add(getRemoveTaskButton(), gridBagConstraints10);
			jPanel7.add(getAddSearchButton(), gridBagConstraints19);
			jPanel7.add(getGoAndStopButton(), gridBagConstraints20);
		}
		return jPanel7;
	}

	/**
	 * This method initializes jScrollPane3
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setPreferredSize(new Dimension(3, 50));
			jScrollPane3.setViewportView(getLogTextArea());
		}
		return jScrollPane3;
	}

	/**
	 * This method initializes logTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	public JTextArea getLogTextArea() {
		if (logTextArea == null) {
			logTextArea = new JTextArea();
			logTextArea.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return logTextArea;
	}

	/**
	 * This method initializes jJToolBarBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJJToolBarBar() {
		if (jJToolBarBar == null) {
			jJToolBarBar = new JToolBar();
			jJToolBarBar.setFloatable(false);
			jJToolBarBar.add(getRefreshProxyButton());
		}
		return jJToolBarBar;
	}

	/**
	 * This method initializes goAndStopButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getGoAndStopButton() {
		if (goAndStopButton == null) {
			goAndStopButton = new JButton();
			goAndStopButton.setEnabled(false);
			goAndStopButton.setText("작업시작");
			goAndStopButton.setFont(new Font("Dialog", Font.PLAIN, 11));
			goAndStopButton.setToolTipText("Go");
		}
		return goAndStopButton;
	}

	/**
	 * This method initializes refreshProxyButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getRefreshProxyButton() {
		if (refreshProxyButton == null) {
			refreshProxyButton = new JButton();
			refreshProxyButton.setIcon(new ImageIcon(getClass().getResource("/img/bullet53318500[1].gif")));
			refreshProxyButton.setEnabled(false);
			refreshProxyButton.setToolTipText("Refresh Proxy");
			refreshProxyButton.setVisible(false);
		}
		return refreshProxyButton;
	}

	/**
	 * This method initializes jPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			TitledBorder titledBorder4 = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ub85c\uadf8", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\uad74\ub9bc", Font.BOLD, 11), new Color(51, 51, 51));
			titledBorder4.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			titledBorder4.setTitle("설정");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.BOTH;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.gridx = 3;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints18.gridx = 2;
			jLabel1 = new JLabel();
			jLabel1.setText("작업 간격(초)");
			jLabel1.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel5 = new JPanel();
			jPanel5.setLayout(new GridBagLayout());
			jPanel5.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel5.setBorder(titledBorder4);
			jPanel5.add(jLabel1, gridBagConstraints18);
			jPanel5.add(get작업간격Slider(), gridBagConstraints17);
		}
		return jPanel5;
	}


	/**
	 * This method initializes 작업간격Slider
	 * 
	 * @return javax.swing.JSlider
	 */
	public JSlider get작업간격Slider() {
		if (작업간격Slider == null) {
			작업간격Slider = new JSlider();
			작업간격Slider.setMaximum(36);
			작업간격Slider.setValue(2);
			작업간격Slider.setPaintLabels(true);
			작업간격Slider.setPaintTicks(true);
			작업간격Slider.setMinorTickSpacing(1);
			작업간격Slider.setSnapToTicks(true);
			작업간격Slider.setEnabled(false);
			작업간격Slider.setMinimum(0);
			Dictionary<Integer, JLabel> dic  = new Hashtable();
			dic.put(new Integer(1),new JLabel("1초"));
			dic.put(new Integer(5),new JLabel("5초"));
			dic.put(new Integer(16),new JLabel("1분"));
			dic.put(new Integer(26),new JLabel("10분"));
			dic.put(new Integer(36),new JLabel("1시간"));
			작업간격Slider.setLabelTable(dic);
		}
		return 작업간격Slider;
	}

	/**
	 * This method initializes addSearchButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getAddSearchButton() {
		if (addSearchButton == null) {
			addSearchButton = new JButton();
			addSearchButton.setEnabled(false);
			addSearchButton.setText("검색결과추가");
			addSearchButton.setFont(new Font("Dialog", Font.PLAIN, 11));
			addSearchButton.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return addSearchButton;
	}

	/**
	 * This method initializes jPanel8	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel8() {
		if (jPanel8 == null) {
			TitledBorder titledBorder5 = BorderFactory.createTitledBorder(null, "\uc791\uc5c5 \ub85c\uadf8", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("\uad74\ub9bc", Font.BOLD, 11), new Color(51, 51, 51));
			titledBorder5.setTitleFont(new Font("Dialog", Font.BOLD, 11));
			titledBorder5.setTitle("진행상황");
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.weightx = 1.0D;
			gridBagConstraints21.gridy = 0;
			jPanel8 = new JPanel();
			jPanel8.setLayout(new GridBagLayout());
			jPanel8.setFont(new Font("Dialog", Font.PLAIN, 11));
			jPanel8.setBorder(titledBorder5);
			jPanel8.add(getJProgressBar(), gridBagConstraints21);
		}
		return jPanel8;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	public JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setStringPainted(true);
			jProgressBar.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jProgressBar;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
