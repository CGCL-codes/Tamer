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

	private JButton �����򰡼�Button = null;

	private JList ����List = null;

	private JPanel ����Panel = null;

	private JButton �2Button = null;

	private JButton �Button = null;

	private JButton �Ű����Ǹ���Button = null;

	private JList ���List = null;

	private JPanel ���Panel = null;

	private JList ����List = null;

	private JPanel ����Panel = null;

	private JList ��������List = null;

	private JPanel ��������Panel = null;

	private JList ����List = null;

	private JPanel �������Panel = null;

	private JButton �ε���ǥ�ø��Button = null;

	private JList ���List = null;

	private JPanel ���Panel = null;

	private JButton �����������缭Button = null;

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
			allWorkButton.setText("��ü�۾�");
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
			deleteAllButton.setText("��ü ����");
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
			workButton.setText("�۾�");
		}
		return workButton;
	}

	/**
	 * This method initializes �����򰡼�Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get�����򰡼�Button() {
		if (�����򰡼�Button == null) {
			�����򰡼�Button = new JButton();
			�����򰡼�Button.setText("��");
			�����򰡼�Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return �����򰡼�Button;
	}

	/**
	 * This method initializes ����List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get����List() {
		if (����List == null) {
			����List = new JList();
			����List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return ����List;
	}

	/**
	 * This method initializes �2Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get�2Button() {
		if (�2Button == null) {
			�2Button = new JButton();
			�2Button.setText("�");
		}
		return �2Button;
	}

	/**
	 * This method initializes �Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get�Button() {
		if (�Button == null) {
			�Button = new JButton();
			�Button.setText("��");
			�Button.setPreferredSize(new Dimension(47, 28));
			�Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return �Button;
	}

	/**
	 * This method initializes �Ű����Ǹ���Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get�Ű����Ǹ���Button() {
		if (�Ű����Ǹ���Button == null) {
			�Ű����Ǹ���Button = new JButton();
			�Ű����Ǹ���Button.setText("��");
			�Ű����Ǹ���Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return �Ű����Ǹ���Button;
	}

	/**
	 * This method initializes ���List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get���List() {
		if (���List == null) {
			���List = new JList();
			���List.setName("���λ���List");
			���List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return ���List;
	}

	/**
	 * This method initializes ����List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get����List() {
		if (����List == null) {
			����List = new JList();
			����List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return ����List;
	}

	/**
	 * This method initializes ��������List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get��������List() {
		if (��������List == null) {
			��������List = new JList();
			��������List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return ��������List;
	}

	/**
	 * This method initializes ����List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get����List() {
		if (����List == null) {
			����List = new JList();
			����List.setModel(new ����ListModel());
			����List.setFont(new Font("Dialog", Font.BOLD, 14));

		}
		return ����List;
	}

	/**
	 * This method initializes �ε���ǥ�ø��Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get�ε���ǥ�ø��Button() {
		if (�ε���ǥ�ø��Button == null) {
			�ε���ǥ�ø��Button = new JButton();
			�ε���ǥ�ø��Button.setText("��");
			�ε���ǥ�ø��Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return �ε���ǥ�ø��Button;
	}

	/**
	 * This method initializes ���List
	 * 
	 * @return javax.swing.JList
	 */
	public JList get���List() {
		if (���List == null) {
			���List = new JList();
			���List.setFont(new Font("Dialog", Font.BOLD, 14));
		}
		return ���List;
	}

	/**
	 * This method initializes �����������缭Button
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton get�����������缭Button() {
		if (�����������缭Button == null) {
			�����������缭Button = new JButton();
			�����������缭Button.setText("��");
			�����������缭Button.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return �����������缭Button;
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
			jPanel.add(get�Button(), new GridBagConstraints());
			jPanel.add(get�����������缭Button(), gridBagConstraints7);
			jPanel.add(get�ε���ǥ�ø��Button(), gridBagConstraints8);
			jPanel.add(get�����򰡼�Button(), gridBagConstraints10);
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
			jPanel1.add(get�������Panel(), gridBagConstraints2);
			jPanel1.add(get����Panel(), gridBagConstraints3);
			jPanel1.add(get���Panel(), gridBagConstraints1);
			jPanel1.add(get����Panel(), gridBagConstraints);
			jPanel1.add(get��������Panel(), gridBagConstraints4);
			jPanel1.add(get���Panel(), gridBagConstraints6);
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
			jPanel2.add(get�2Button(), new GridBagConstraints());
			jPanel2.add(get�Ű����Ǹ���Button(), gridBagConstraints9);
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
			jScrollPane.setViewportView(get����List());
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
			jScrollPane1.setViewportView(get����List());
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
			jScrollPane2.setViewportView(get���List());
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
			jScrollPane3.setViewportView(get����List());
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
			jScrollPane5.setViewportView(get��������List());
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
			jScrollPane6.setViewportView(get���List());
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
	 * This method initializes ����Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get����Panel() {
		if (����Panel == null) {
			TitledBorder titledBorder1 = BorderFactory.createTitledBorder(null,
					"\uc0ac\uac74\ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51,
							51, 51));
			titledBorder1.setTitleFont(new Font("Dialog", Font.BOLD, 18));
			titledBorder1.setTitle("������");
			����Panel = new JPanel();
			����Panel.setLayout(new BorderLayout());
			����Panel.setBorder(titledBorder1);
			����Panel.add(getJScrollPane1(), BorderLayout.CENTER);
			����Panel.add(getUndoButton(), BorderLayout.SOUTH);
		}
		return ����Panel;
	}

	/**
	 * This method initializes ���Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get���Panel() {
		if (���Panel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.weightx = 1.0;
			���Panel = new JPanel();
			���Panel.setLayout(new GridBagLayout());
			���Panel.setBorder(BorderFactory.createTitledBorder(null, "\ubaa9\ub85d",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 18), new Color(51, 51, 51)));
			���Panel.add(getJScrollPane6(), gridBagConstraints5);
		}
		return ���Panel;
	}

	/**
	 * This method initializes ����Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get����Panel() {
		if (����Panel == null) {
			TitledBorder titledBorder3 = BorderFactory.createTitledBorder(null,
					"\uc0ac\uac74\ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51,
							51, 51));
			titledBorder3.setTitleFont(new Font("Dialog", Font.BOLD, 18));
			titledBorder3.setTitle("���Ǹ��");
			����Panel = new JPanel();
			����Panel.setLayout(new BorderLayout());
			����Panel.setBorder(titledBorder3);
			����Panel.add(getJScrollPane3(), BorderLayout.CENTER);
			����Panel.add(getJPanel2(), BorderLayout.SOUTH);
		}
		return ����Panel;
	}

	/**
	 * This method initializes ��������Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get��������Panel() {
		if (��������Panel == null) {
			��������Panel = new JPanel();
			��������Panel.setLayout(new BorderLayout());
			��������Panel.setBorder(BorderFactory.createTitledBorder(null, "\ubb3c\uac74 \uc815\ubcf4",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 18), new Color(51, 51, 51)));
			��������Panel.add(getJScrollPane5(), BorderLayout.CENTER);
		}
		return ��������Panel;
	}

	/**
	 * This method initializes �������Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get�������Panel() {
		if (�������Panel == null) {
			TitledBorder titledBorder = BorderFactory.createTitledBorder(null,
					"\uc0ac\uac74\ubaa9\ub85d", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 18), new Color(51,
							51, 51));
			titledBorder.setTitle("�������");
			�������Panel = new JPanel();
			�������Panel.setLayout(new BorderLayout());
			�������Panel.setBorder(titledBorder);
			�������Panel.add(getJScrollPane(), BorderLayout.CENTER);
			�������Panel.add(getJPanel3(), BorderLayout.SOUTH);
		}
		return �������Panel;
	}

	/**
	 * This method initializes ���Panel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel get���Panel() {
		if (���Panel == null) {
			���Panel = new JPanel();
			���Panel.setLayout(new BorderLayout());
			���Panel.setBorder(BorderFactory.createTitledBorder(null, "\uc0ac\uac74\ubaa9\ub85d",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 18), new Color(51, 51, 51)));
			���Panel.add(getJScrollPane2(), BorderLayout.CENTER);
			���Panel.add(getJPanel(), BorderLayout.SOUTH);
		}
		return ���Panel;
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
