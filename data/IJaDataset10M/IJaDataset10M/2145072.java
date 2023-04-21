package net.narusas.aceauction.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public JLabel bgWorkLabel = null;

	private JButton allWorkButton = null;

	private JButton deleteAllButton = null;

	private JTextField endDayTextField = null;

	private JTextField endMonthTextField = null;

	private JTextField endYearTextField = null;

	private JPanel infoPanel = null;

	private JTable infoTable = null;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JPanel jPanel3 = null;

	private JPanel jPanel4 = null;

	private JPanel jPanel5 = null;

	private JPanel jPanel6 = null;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	private JScrollPane jScrollPane2 = null;

	private JScrollPane jScrollPane3 = null;

	private JScrollPane jScrollPane4 = null;

	private JScrollPane jScrollPane5 = null;

	private JScrollPane jScrollPane6 = null;

	private JSplitPane jSplitPane = null;

	private JTextField startDayTextField = null;

	private JTextField startMonthTextField = null;

	private JTextField startYearTextField = null;

	private JButton undoButton = null;

	private JButton workButton = null;

	private JButton 감정평가서Button = null;

	private JList 담당계List = null;

	private JPanel 담당계Panel = null;

	private JButton 등본2Button = null;

	private JButton 등본Button = null;

	private JButton 매각물건명세서Button = null;

	private JList 목록List = null;

	private JPanel 목록Panel = null;

	private JList 물건List = null;

	private JPanel 물건Panel = null;

	private JList 물건정보List = null;

	private JPanel 물건정보Panel = null;

	private JList 법원List = null;

	private JPanel 법원목록Panel = null;

	private JButton 부동산표시목록Button = null;

	private JList 사건List = null;

	private JPanel 사건Panel = null;

	private JButton 점유관계조사서Button = null;

	/**
	 * This is the default constructor
	 * 
	 * @param string
	 */
	public ConsoleFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes allWorkButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getAllWorkButton() {
		if (allWorkButton == null) {
			allWorkButton = new JButton();
			allWorkButton.setText("전체작업");
		}
		return allWorkButton;
	}

	/**
	 * This method initializes deleteAllButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getDeleteAllButton() {
		if (deleteAllButton == null) {
			deleteAllButton = new JButton();
			deleteAllButton.setText("전체 삭제");
			deleteAllButton.setEnabled(false);
			deleteAllButton.setVisible(false);
		}
		return deleteAllButton;
	}

	/**
	 * This method initializes endDayTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getEndDayTextField() {
		if (endDayTextField == null) {
			endDayTextField = new JTextField();
			endDayTextField.setPreferredSize(new Dimension(20, 22));
			endDayTextField.setText("55");
		}
		return endDayTextField;
	}

	/**
	 * This method initializes endMonthTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getEndMonthTextField() {
		if (endMonthTextField == null) {
			endMonthTextField = new JTextField();
			endMonthTextField.setPreferredSize(new Dimension(20, 22));
			endMonthTextField.setText("55");
		}
		return endMonthTextField;
	}

	/**
	 * This method initializes endYearTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getEndYearTextField() {
		if (endYearTextField == null) {
			endYearTextField = new JTextField();
			endYearTextField.setText("2007");
			endYearTextField.setPreferredSize(new Dimension(50, 22));
		}
		return endYearTextField;
	}

	/**
	 * This method initializes infoTable
	 * 
	 * @return javax.swing.JTable
	 */
	public JTable getInfoTable() {
		if (infoTable == null) {
			infoTable = new JTable();
			infoTable.setFont(new Font("Dialog", Font.PLAIN, 14));
			infoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return infoTable;
	}

	/**
	 * This method initializes startDayTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getStartDayTextField() {
		if (startDayTextField == null) {
			startDayTextField = new JTextField();
			startDayTextField.setPreferredSize(new Dimension(20, 22));
			startDayTextField.setText("55");
		}
		return startDayTextField;
	}

	/**
	 * This method initializes startMonthTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getStartMonthTextField() {
		if (startMonthTextField == null) {
			startMonthTextField = new JTextField();
			startMonthTextField.setPreferredSize(new Dimension(20, 22));
			startMonthTextField.setText("55");
		}
		return startMonthTextField;
	}

	/**
	 * This method initializes startYearTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getStartYearTextField() {
		if (startYearTextField == null) {
			startYearTextField = new JTextField();
			startYearTextField.setPreferredSize(new Dimension(50, 22));
			startYearTextField.setText("2007");
		}
		return startYearTextField;
	}

	/**
	 * This method initializes undoButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getUndoButton() {
		if (undoButton == null) {
			undoButton = new JButton();
			undoButton.setText("Undo");
		}
		return undoButton;
	}

	/**
	 * This method initializes workButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getWorkButton() {
		if (workButton == null) {
			workButton = new JButton();
			workButton.setText("작업");
		}
		return workButton;
	}

	/**
	 * This method initializes 감정평가서Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get감정평가서Button() {
		if (감정평가서Button == null) {
			감정평가서Button = new JButton();
			감정평가서Button.setText("감");
			감정평가서Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return 감정평가서Button;
	}

	/**
	 * This method initializes 담당계List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get담당계List() {
		if (담당계List == null) {
			담당계List = new JList();
			담당계List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return 담당계List;
	}

	/**
	 * This method initializes 등본2Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get등본2Button() {
		if (등본2Button == null) {
			등본2Button = new JButton();
			등본2Button.setText("등본");
		}
		return 등본2Button;
	}

	/**
	 * This method initializes 등본Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get등본Button() {
		if (등본Button == null) {
			등본Button = new JButton();
			등본Button.setText("등");
			등본Button.setPreferredSize(new Dimension(47, 28));
			등본Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return 등본Button;
	}

	/**
	 * This method initializes 매각물건명세서Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get매각물건명세서Button() {
		if (매각물건명세서Button == null) {
			매각물건명세서Button = new JButton();
			매각물건명세서Button.setText("매");
			매각물건명세서Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return 매각물건명세서Button;
	}

	/**
	 * This method initializes 목록List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get목록List() {
		if (목록List == null) {
			목록List = new JList();
			목록List.setName("세부사항List");
			목록List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return 목록List;
	}

	/**
	 * This method initializes 물건List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get물건List() {
		if (물건List == null) {
			물건List = new JList();
			물건List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return 물건List;
	}

	/**
	 * This method initializes 물건정보List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get물건정보List() {
		if (물건정보List == null) {
			물건정보List = new JList();
			물건정보List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return 물건정보List;
	}

	/**
	 * This method initializes 법원List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get법원List() {
		if (법원List == null) {
			법원List = new JList();
			법원List.setModel(new 법원ListModel());
			법원List.setFont(new Font("Dialog", Font.BOLD, 14));

		}
		return 법원List;
	}

	/**
	 * This method initializes 부동산표시목록Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get부동산표시목록Button() {
		if (부동산표시목록Button == null) {
			부동산표시목록Button = new JButton();
			부동산표시목록Button.setText("부");
			부동산표시목록Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return 부동산표시목록Button;
	}

	/**
	 * This method initializes 사건List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get사건List() {
		if (사건List == null) {
			사건List = new JList();
			사건List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return 사건List;
	}

	/**
	 * This method initializes 점유관계조사서Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get점유관계조사서Button() {
		if (점유관계조사서Button == null) {
			점유관계조사서Button = new JButton();
			점유관계조사서Button.setText("점");
			점유관계조사서Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return 점유관계조사서Button;
	}

	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new JPanel();
			infoPanel.setLayout(new BorderLayout());
			infoPanel.setBorder(BorderFactory.createTitledBorder(null, "\uc815\ubcf4",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			infoPanel.add(getJScrollPane4(), BorderLayout.CENTER);
			infoPanel.add(getJPanel4(), BorderLayout.SOUTH);
		}
		return infoPanel;
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
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
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
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 3;
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;
			gridBagConstraints8.gridy = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(get등본Button(), new GridBagConstraints());
			jPanel.add(get점유관계조사서Button(), gridBagConstraints7);
			jPanel.add(get부동산표시목록Button(), gridBagConstraints8);
			jPanel.add(get감정평가서Button(), gridBagConstraints10);
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
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0D;
			gridBagConstraints6.weightx = 2.0D;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.weightx = 1.0D;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.weightx = 1.0D;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0D;
			gridBagConstraints2.weightx = 1.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.weightx = 0.1D;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0D;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.weightx = 0.3D;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0D;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(get법원목록Panel(), gridBagConstraints2);
			jPanel1.add(get담당계Panel(), gridBagConstraints3);
			jPanel1.add(get사건Panel(), gridBagConstraints1);
			jPanel1.add(get물건Panel(), gridBagConstraints);
			jPanel1.add(get물건정보Panel(), gridBagConstraints4);
			jPanel1.add(get목록Panel(), gridBagConstraints6);
			jPanel1.add(getJPanel6(), gridBagConstraints12);
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
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 0;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(get등본2Button(), new GridBagConstraints());
			jPanel2.add(get매각물건명세서Button(), gridBagConstraints9);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridwidth = 31;
			gridBagConstraints19.gridy = 2;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints17.gridy = 1;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.gridx = 2;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints16.gridy = 1;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.gridx = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.gridx = 2;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridx = 0;
			jPanel3 = new JPanel();
			jPanel3.setLayout(new GridBagLayout());
			jPanel3.add(getStartYearTextField(), gridBagConstraints11);
			jPanel3.add(getStartMonthTextField(), gridBagConstraints13);
			jPanel3.add(getStartDayTextField(), gridBagConstraints14);
			jPanel3.add(getEndYearTextField(), gridBagConstraints15);
			jPanel3.add(getEndMonthTextField(), gridBagConstraints16);
			jPanel3.add(getEndDayTextField(), gridBagConstraints17);
			jPanel3.add(getJPanel5(), gridBagConstraints19);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			bgWorkLabel = new JLabel();
			bgWorkLabel.setText("JLabel");
			jPanel4 = new JPanel();
			jPanel4.setLayout(new GridBagLayout());
			jPanel4.add(bgWorkLabel, new GridBagConstraints());
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new FlowLayout());
			jPanel5.add(getAllWorkButton(), null);
			jPanel5.add(getWorkButton(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new GridBagLayout());
			jPanel6.add(getDeleteAllButton(), new GridBagConstraints());
		}
		return jPanel6;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(get법원List());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(get담당계List());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jScrollPane2
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(get사건List());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jScrollPane3
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setViewportView(get물건List());
		}
		return jScrollPane3;
	}

	/**
	 * This method initializes jScrollPane4
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane4() {
		if (jScrollPane4 == null) {
			jScrollPane4 = new JScrollPane();
			jScrollPane4.setViewportView(getInfoTable());
		}
		return jScrollPane4;
	}

	/**
	 * This method initializes jScrollPane5
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane5() {
		if (jScrollPane5 == null) {
			jScrollPane5 = new JScrollPane();
			jScrollPane5.setViewportView(get물건정보List());
		}
		return jScrollPane5;
	}

	/**
	 * This method initializes jScrollPane6
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane6() {
		if (jScrollPane6 == null) {
			jScrollPane6 = new JScrollPane();
			jScrollPane6.setViewportView(get목록List());
		}
		return jScrollPane6;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setTopComponent(getJPanel1());
			jSplitPane.setBottomComponent(getInfoPanel());
			jSplitPane.setDividerLocation(400);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes 담당계Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get담당계Panel() {
		if (담당계Panel == null) {
			TitledBorder titledBorder1 = BorderFactory.createTitledBorder(null,
					"\uc0ac\uac74\ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51,
							51, 51));
			titledBorder1.setTitleFont(new Font("Dialog", Font.BOLD, 18));
			titledBorder1.setTitle("담당계목록");
			담당계Panel = new JPanel();
			담당계Panel.setLayout(new BorderLayout());
			담당계Panel.setBorder(titledBorder1);
			담당계Panel.add(getJScrollPane1(), BorderLayout.CENTER);
			담당계Panel.add(getUndoButton(), BorderLayout.SOUTH);
		}
		return 담당계Panel;
	}

	/**
	 * This method initializes 목록Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get목록Panel() {
		if (목록Panel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.weightx = 1.0;
			목록Panel = new JPanel();
			목록Panel.setLayout(new GridBagLayout());
			목록Panel.setBorder(BorderFactory.createTitledBorder(null, "\ubaa9\ub85d",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 18), new Color(51, 51, 51)));
			목록Panel.add(getJScrollPane6(), gridBagConstraints5);
		}
		return 목록Panel;
	}

	/**
	 * This method initializes 물건Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get물건Panel() {
		if (물건Panel == null) {
			TitledBorder titledBorder3 = BorderFactory.createTitledBorder(null,
					"\uc0ac\uac74\ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51,
							51, 51));
			titledBorder3.setTitleFont(new Font("Dialog", Font.BOLD, 18));
			titledBorder3.setTitle("물건목록");
			물건Panel = new JPanel();
			물건Panel.setLayout(new BorderLayout());
			물건Panel.setBorder(titledBorder3);
			물건Panel.add(getJScrollPane3(), BorderLayout.CENTER);
			물건Panel.add(getJPanel2(), BorderLayout.SOUTH);
		}
		return 물건Panel;
	}

	/**
	 * This method initializes 물건정보Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get물건정보Panel() {
		if (물건정보Panel == null) {
			물건정보Panel = new JPanel();
			물건정보Panel.setLayout(new BorderLayout());
			물건정보Panel.setBorder(BorderFactory.createTitledBorder(null, "\ubb3c\uac74 \uc815\ubcf4",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 18), new Color(51, 51, 51)));
			물건정보Panel.add(getJScrollPane5(), BorderLayout.CENTER);
		}
		return 물건정보Panel;
	}

	/**
	 * This method initializes 법원목록Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get법원목록Panel() {
		if (법원목록Panel == null) {
			TitledBorder titledBorder = BorderFactory.createTitledBorder(null,
					"\uc0ac\uac74\ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 18), new Color(51,
							51, 51));
			titledBorder.setTitle("법원목록");
			법원목록Panel = new JPanel();
			법원목록Panel.setLayout(new BorderLayout());
			법원목록Panel.setBorder(titledBorder);
			법원목록Panel.add(getJScrollPane(), BorderLayout.CENTER);
			법원목록Panel.add(getJPanel3(), BorderLayout.SOUTH);
		}
		return 법원목록Panel;
	}

	/**
	 * This method initializes 사건Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get사건Panel() {
		if (사건Panel == null) {
			사건Panel = new JPanel();
			사건Panel.setLayout(new BorderLayout());
			사건Panel.setBorder(BorderFactory.createTitledBorder(null, "\uc0ac\uac74\ubaa9\ub85d",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 18), new Color(51, 51, 51)));
			사건Panel.add(getJScrollPane2(), BorderLayout.CENTER);
			사건Panel.add(getJPanel(), BorderLayout.SOUTH);
		}
		return 사건Panel;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1097, 863);
		this.setContentPane(getJContentPane());
		this.setTitle("Ace Auction");
	}

} // @jve:decl-index=0:visual-constraint="10,10"
