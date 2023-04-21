package net.mikaboshi.servlet.monitor.viewer;

import static javax.swing.JOptionPane.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.mikaboshi.servlet.monitor.LogEntry;
import net.mikaboshi.servlet.monitor.LogFileAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletLogViewerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static Log systemLogger = LogFactory.getLog(ServletLogViewerFrame.class);  //  @jve:decl-index=0:
	private File logFile;  //  @jve:decl-index=0:
	private LogFileAccessor logAccessor;
	
	private JPanel jContentPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu fileMenu = null;
	private JSplitPane mainSplitPane = null;
	private JToolBar statusToolBar = null;
	private JPanel fileOpenPanel = null;
	private LogTableScrollPane logTableScrollPane = null;

	private JLabel dragFileHereLabel = null;

	private JMenu viewMenu = null;

	private LogDetailTabbedPane logDetailTabbedPane = null;

	private JPanel mainPanel = null;

	private JPanel logTablePanel = null;

	private JToggleButton readToggleButton = null;

	private JToggleButton suspendToggleButton = null;

	private JPanel buttonsPanel = null;

	private JButton filterButton = null;

	private JButton clearButton = null;

	private JButton reopenButton = null;

	private JPanel lastLogTimestampPanel = null;

	private JLabel lastLogDateTimeLabel = null;

	private JPanel rowsCountPanel = null;

	private JLabel rowsCountCaptionLabel = null;

	private JPanel statusMessagePanel = null;

	private JLabel statusMessageLabel = null;

	private JPanel blankStatusbarPanel = null;

	private JLabel statusBarSeparatorLabel1 = null;

	private JLabel statusBarSeparatorLabel2 = null;

	private JMenu toolMenu = null;

	private JPanel buttonsSeparatorPanel = null;

	private JLabel buttonsSeparatorLabel = null;

	private JLabel searchLabel = null;

	private JTextField searchTextField = null;

	private JButton searchNextButton = null;

	private JButton searchPreviousButton = null;

	private JCheckBox searchRegExpCheckBox = null;

	private JCheckBox searchCaseSensitiveCheckBox = null;

	private JCheckBox searchMarkCheckBox = null;

	private JCheckBox searchCirculatingCheckBox = null;

	private JLabel lastLogDateTimeCaptionLabel = null;

	private JLabel selectedRowsCountLabel = null;

	private JLabel rowsCountSeparatorLabel1 = null;

	private JLabel visibleRowsCountLabel = null;

	private JLabel rowsCountSeparatorLabel2 = null;

	private JLabel allRowsCountLabel = null;

	private JMenuItem openLogFileMenuItem = null;

	private JMenuItem reopenLogFileMenuItem = null;

	private JMenuItem exportLogMenuItem = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem columnConfigMenuItem = null;

	private JCheckBoxMenuItem statusBarCheckBoxMenuItem = null;

	private JCheckBoxMenuItem autoScrollCheckBoxMenuItem = null;

	private JMenuItem viewerPreferencesMenuItem = null;

	private JMenuItem loggerConfigMenuItem = null;

	private JMenuItem versionInfoMenuItem = null;

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getViewMenu());
			jJMenuBar.add(getToolMenu());
		}
		
		return jJMenuBar;
	}

	/**
	 * This method initializes fileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("�t�@�C��(F)");
			fileMenu.setActionCommand("�t�@�C��(F)");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			fileMenu.add(getOpenLogFileMenuItem());
			fileMenu.add(getReopenLogFileMenuItem());
			fileMenu.addSeparator();
			fileMenu.add(getExportLogMenuItem());
			fileMenu.addSeparator();
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes mainSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane();
			mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			mainSplitPane.setName("mainSplitPane");
			mainSplitPane.setOneTouchExpandable(true);
			mainSplitPane.setTopComponent(getLogTablePanel());
			mainSplitPane.setBottomComponent(getLogDetailTabbedPane());
			mainSplitPane.setDividerLocation((int) (getHeight() * 0.3));
		}
		return mainSplitPane;
	}

	/**
	 * This method initializes statusToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getStatusToolBar() {
		if (statusToolBar == null) {
			statusBarSeparatorLabel2 = new JLabel();
			statusBarSeparatorLabel2.setText("");
			statusBarSeparatorLabel2.setPreferredSize(new Dimension(2, 26));
			statusBarSeparatorLabel2.setIcon(new ImageIcon(getClass().getResource("separator.png")));
			statusBarSeparatorLabel1 = new JLabel();
			statusBarSeparatorLabel1.setText("");
			statusBarSeparatorLabel1.setPreferredSize(new Dimension(2, 26));
			statusBarSeparatorLabel1.setIcon(new ImageIcon(getClass().getResource("separator.png")));
			statusToolBar = new JToolBar();
			statusToolBar.setFloatable(false);
			statusToolBar.setPreferredSize(new Dimension(148, 26));
			statusToolBar.add(getStatusMessagePanel());
			statusToolBar.add(statusBarSeparatorLabel1);
			statusToolBar.add(getLastLogTimestampPanel());
			statusToolBar.add(statusBarSeparatorLabel2);
			statusToolBar.add(getRowsCountPanel());
			statusToolBar.add(getBlankStatusbarPanel());
		}
		return statusToolBar;
	}

	/**
	 * This method initializes fileOpenPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getFileOpenPanel() {
		if (fileOpenPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.gridx = 0;
			dragFileHereLabel = new JLabel();
			dragFileHereLabel.setText("�����Ƀ��O�t�@�C�����h���b�v");
			dragFileHereLabel.setName("dropFileHereLabel");
			fileOpenPanel = new JPanel();
			fileOpenPanel.setLayout(new GridBagLayout());
			fileOpenPanel.setName("fileOpenPanel");
			fileOpenPanel.add(dragFileHereLabel, gridBagConstraints);
		}
		return fileOpenPanel;
	}

	/**
	 * This method initializes viewMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getViewMenu() {
		if (viewMenu == null) {
			viewMenu = new JMenu();
			viewMenu.setText("�\��(V)");
			viewMenu.setMnemonic(KeyEvent.VK_V);
			viewMenu.setActionCommand("�\��(V)");
			viewMenu.add(getColumnConfigMenuItem());
			viewMenu.add(getStatusBarCheckBoxMenuItem());
			viewMenu.add(getAutoScrollCheckBoxMenuItem());
		}
		return viewMenu;
	}

	private LogDetailTabbedPane getLogDetailTabbedPane() {
		if (logDetailTabbedPane == null) {
			logDetailTabbedPane = new LogDetailTabbedPane(1);
		}
		return logDetailTabbedPane;
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new CardLayout());
			mainPanel.add(getMainSplitPane(), getMainSplitPane().getName());
		}
		return mainPanel;
	}

	/**
	 * This method initializes logTablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLogTablePanel() {
		if (logTablePanel == null) {
			logTablePanel = new JPanel();
			logTablePanel.setLayout(new CardLayout());
			logTablePanel.setName("logTablePanel");
			logTablePanel.add(getFileOpenPanel(), getFileOpenPanel().getName());
			logTablePanel.add(getLogTableScrollPane(), getLogTableScrollPane().getName());
		}
		return logTablePanel;
	}

	/**
	 * This method initializes readToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getReadToggleButton() {
		if (readToggleButton == null) {
			readToggleButton = new JToggleButton();
			readToggleButton.setIcon(new ImageIcon(getClass().getResource("read.png")));
			readToggleButton.setToolTipText("���O�̓ǂݍ��݂��J�n����");
			readToggleButton.setSelected(true);
			readToggleButton.setPreferredSize(new Dimension(30, 24));
		}
		return readToggleButton;
	}

	/**
	 * This method initializes suspendToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getSuspendToggleButton() {
		if (suspendToggleButton == null) {
			suspendToggleButton = new JToggleButton();
			suspendToggleButton.setToolTipText("���O�̓ǂݍ��݂��~����");
			suspendToggleButton.setPreferredSize(new Dimension(30, 24));
			suspendToggleButton.setIcon(new ImageIcon(getClass().getResource("suspend.png")));
		}
		return suspendToggleButton;
	}

	/**
	 * This method initializes buttonsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			searchLabel = new JLabel();
			searchLabel.setText("�����F");
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(5);
			buttonsPanel = new JPanel();
			buttonsPanel.setPreferredSize(new Dimension(180, 34));
			buttonsPanel.setLayout(flowLayout);
			buttonsPanel.add(getReadToggleButton(), null);
			buttonsPanel.add(getSuspendToggleButton(), null);
			buttonsPanel.add(getFilterButton(), null);
			buttonsPanel.add(getClearButton(), null);
			buttonsPanel.add(getReopenButton(), null);
			buttonsPanel.add(getButtonsSeparatorPanel(), null);
			buttonsPanel.add(searchLabel, null);
			buttonsPanel.add(getSearchTextField(), null);
			buttonsPanel.add(getSearchNextButton(), null);
			buttonsPanel.add(getSearchPreviousButton(), null);
			buttonsPanel.add(getSearchMarkCheckBox(), null);
			buttonsPanel.add(getSearchRegExpCheckBox(), null);
			buttonsPanel.add(getSearchCaseSensitiveCheckBox(), null);
			buttonsPanel.add(getSearchCirculatingCheckBox(), null);
			
			ButtonGroup switchReadGrp = new ButtonGroup();
			switchReadGrp.add(getReadToggleButton());
			switchReadGrp.add(getSuspendToggleButton());
		}
		return buttonsPanel;
	}

	/**
	 * This method initializes filterButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton();
			filterButton.setToolTipText("�t�B���^");
			filterButton.setPreferredSize(new Dimension(30, 24));
			filterButton.setIcon(new ImageIcon(getClass().getResource("filter.png")));
			
			filterButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JDialog dialog = new FilterDialog(ServletLogViewerFrame.this);
					dialog.setLocationRelativeTo(ServletLogViewerFrame.this);
					dialog.setVisible(true);
				}
			});
		}
		return filterButton;
	}

	/**
	 * This method initializes clearButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton();
			clearButton.setToolTipText("�ǂݍ��񂾃��O�̃N���A");
			clearButton.setPreferredSize(new Dimension(30, 24));
			clearButton.setIcon(new ImageIcon(getClass().getResource("clear.png")));
			
			clearButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getLogTableModel().reset();
					updateStatusBar();
					selectLogEntry(null);
					FilterComboItems.getInstance().reset();
				}
			});
		}
		return clearButton;
	}

	/**
	 * This method initializes reopenButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getReopenButton() {
		if (reopenButton == null) {
			reopenButton = new JButton();
			reopenButton.setToolTipText("���O�t�@�C�����J������");
			reopenButton.setPreferredSize(new Dimension(30, 24));
			reopenButton.setIcon(new ImageIcon(getClass().getResource("reopen.png")));
			
			reopenButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (logFile != null) {
						openLogFile();
					}
				}
			});
		}
		return reopenButton;
	}

	/**
	 * This method initializes lastLogTimestampPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLastLogTimestampPanel() {
		if (lastLogTimestampPanel == null) {
			lastLogDateTimeCaptionLabel = new JLabel();
			lastLogDateTimeCaptionLabel.setText("�ŐV�̃��O�̓��� :");
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout1.setHgap(10);
			flowLayout1.setVgap(2);
			lastLogDateTimeLabel = new JLabel();
			lastLogDateTimeLabel.setText("");
			lastLogTimestampPanel = new JPanel();
			lastLogTimestampPanel.setLayout(flowLayout1);
			lastLogTimestampPanel.add(lastLogDateTimeCaptionLabel, null);
			lastLogTimestampPanel.add(lastLogDateTimeLabel, null);
		}
		return lastLogTimestampPanel;
	}

	/**
	 * This method initializes rowsCountPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRowsCountPanel() {
		if (rowsCountPanel == null) {
			allRowsCountLabel = new JLabel();
			allRowsCountLabel.setText("-");
			rowsCountSeparatorLabel2 = new JLabel();
			rowsCountSeparatorLabel2.setText("/");
			visibleRowsCountLabel = new JLabel();
			visibleRowsCountLabel.setText("-");
			rowsCountSeparatorLabel1 = new JLabel();
			rowsCountSeparatorLabel1.setText("/");
			selectedRowsCountLabel = new JLabel();
			selectedRowsCountLabel.setText("-");
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout3.setHgap(10);
			flowLayout3.setVgap(2);
			rowsCountCaptionLabel = new JLabel();
			rowsCountCaptionLabel.setText("�I���s / �\���s / �S�s :");
			rowsCountPanel = new JPanel();
			rowsCountPanel.setLayout(flowLayout3);
			rowsCountPanel.add(rowsCountCaptionLabel, null);
			rowsCountPanel.add(selectedRowsCountLabel, null);
			rowsCountPanel.add(rowsCountSeparatorLabel1, null);
			rowsCountPanel.add(visibleRowsCountLabel, null);
			rowsCountPanel.add(rowsCountSeparatorLabel2, null);
			rowsCountPanel.add(allRowsCountLabel, null);
		}
		return rowsCountPanel;
	}

	/**
	 * This method initializes statusMessagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStatusMessagePanel() {
		if (statusMessagePanel == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout2.setHgap(10);
			flowLayout2.setVgap(2);
			statusMessageLabel = new JLabel();
			statusMessageLabel.setText("");
			statusMessageLabel.setPreferredSize(new Dimension(200, 16));
			statusMessagePanel = new JPanel();
			statusMessagePanel.setLayout(flowLayout2);
			statusMessagePanel.add(statusMessageLabel, null);
		}
		return statusMessagePanel;
	}

	/**
	 * This method initializes blankStatusbarPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBlankStatusbarPanel() {
		if (blankStatusbarPanel == null) {
			blankStatusbarPanel = new JPanel();
			blankStatusbarPanel.setLayout(new GridBagLayout());
		}
		return blankStatusbarPanel;
	}

	/**
	 * This method initializes toolMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getToolMenu() {
		if (toolMenu == null) {
			toolMenu = new JMenu();
			toolMenu.setText("�c�[��(T)");
			toolMenu.setMnemonic(KeyEvent.VK_T);
			toolMenu.setActionCommand("�c�[��(T)");
			toolMenu.add(getViewerPreferencesMenuItem());
			toolMenu.add(getLoggerConfigMenuItem());
			toolMenu.add(getVersionInfoMenuItem());
		}
		return toolMenu;
	}

	/**
	 * This method initializes buttonsSeparatorPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonsSeparatorPanel() {
		if (buttonsSeparatorPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			buttonsSeparatorLabel = new JLabel();
			buttonsSeparatorLabel.setText("");
			buttonsSeparatorLabel.setIcon(new ImageIcon(getClass().getResource("/net/mikaboshi/servlet/monitor/viewer/separator.png")));
			buttonsSeparatorPanel = new JPanel();
			buttonsSeparatorPanel.setLayout(new GridBagLayout());
			buttonsSeparatorPanel.add(buttonsSeparatorLabel, gridBagConstraints1);
		}
		return buttonsSeparatorPanel;
	}

	/**
	 * This method initializes searchTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchTextField() {
		if (searchTextField == null) {
			searchTextField = new JTextField();
			searchTextField.setPreferredSize(new Dimension(160, 20));
			searchTextField.setToolTipText("�e�L�X�g�{�b�N�X����Enter�L�[����������Ǝ������AShift+Enter����������ƑO�������s�Ȃ��܂�");
			
			searchTextField.addKeyListener(new KeyAdapter() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					
					boolean directionNext;
					boolean searchCurrentFirst;
					
					if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
						
						// Enter�L�[�����Ŏ��AShift+Enter�őO��T��
						directionNext = 
							(e.getModifiers() & KeyEvent.SHIFT_MASK) != KeyEvent.SHIFT_MASK;
						
						searchCurrentFirst = false;
					
					} else {
						// Enter�ȊO�̃L�[�������ɍČ������s��
						directionNext = true;
						searchCurrentFirst = true;
					}
					
					boolean found = getLogTableScrollPane().search(
							getSearchTextField().getText(),
							directionNext,
							getSearchMarkCheckBox().isSelected(),
							getSearchRegExpCheckBox().isSelected(),
							getSearchCaseSensitiveCheckBox().isSelected(),
							true,
							searchCurrentFirst);
					
					setSearchResult(found);
					
					repaint();
				}
			});
			
			// Ctrl+F�Ō����e�L�X�g�Ƀt�H�[�J�X�����킹��
			searchTextField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK), "f");

			searchTextField.getActionMap().put("f", new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					searchTextField.requestFocus();
				}
			});
		}
		return searchTextField;
	}

	/**
	 * This method initializes searchNextButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSearchNextButton() {
		if (searchNextButton == null) {
			searchNextButton = new JButton();
			searchNextButton.setText("��(N)");
			searchNextButton.setMnemonic(KeyEvent.VK_N);
			searchNextButton.setPreferredSize(new Dimension(57, 24));
			searchNextButton.setActionCommand("");
			searchNextButton.setFont(new Font("Dialog", Font.PLAIN, 10));
			
			searchNextButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean found = getLogTableScrollPane().search(
							getSearchTextField().getText(),
							true,
							getSearchMarkCheckBox().isSelected(),
							getSearchRegExpCheckBox().isSelected(),
							getSearchCaseSensitiveCheckBox().isSelected(),
							getSearchCirculatingCheckBox().isSelected(),
							false);
					
					setSearchResult(found);
					
					repaint();
				}
			});
		}
		return searchNextButton;
	}

	/**
	 * This method initializes searchPreviousButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSearchPreviousButton() {
		if (searchPreviousButton == null) {
			searchPreviousButton = new JButton();
			searchPreviousButton.setText("�O(P)");
			searchPreviousButton.setMnemonic(KeyEvent.VK_P);
			searchPreviousButton.setFont(new Font("Dialog", Font.PLAIN, 10));
			
			searchPreviousButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean found = getLogTableScrollPane().search(
							getSearchTextField().getText(),
							false,
							getSearchMarkCheckBox().isSelected(),
							getSearchRegExpCheckBox().isSelected(),
							getSearchCaseSensitiveCheckBox().isSelected(),
							getSearchCirculatingCheckBox().isSelected(),
							false);
					
					setSearchResult(found);
					
					repaint();
				}
			});
		}
		return searchPreviousButton;
	}

	/**
	 * This method initializes searchRegExpCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSearchRegExpCheckBox() {
		if (searchRegExpCheckBox == null) {
			searchRegExpCheckBox = new JCheckBox();
			searchRegExpCheckBox.setText("���K�\��");
			searchRegExpCheckBox.setFont(new Font("Dialog", Font.PLAIN, 10));
			
			searchRegExpCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					getLogTableScrollPane().setMark(
							getSearchTextField().getText(),
							getSearchMarkCheckBox().isSelected(),
							getSearchRegExpCheckBox().isSelected(),
							getSearchCaseSensitiveCheckBox().isSelected());
					
					repaint();
				}
			});
		}
		return searchRegExpCheckBox;
	}

	/**
	 * This method initializes searchCaseSensitiveCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSearchCaseSensitiveCheckBox() {
		if (searchCaseSensitiveCheckBox == null) {
			searchCaseSensitiveCheckBox = new JCheckBox();
			searchCaseSensitiveCheckBox.setText("��/�����");
			searchCaseSensitiveCheckBox.setFont(new Font("Dialog", Font.PLAIN, 10));
			searchCaseSensitiveCheckBox.setToolTipText("�啶��/����������ʂ���");
			
			searchCaseSensitiveCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					getLogTableScrollPane().setMark(
							getSearchTextField().getText(),
							getSearchMarkCheckBox().isSelected(),
							getSearchRegExpCheckBox().isSelected(),
							getSearchCaseSensitiveCheckBox().isSelected());
					
					repaint();
				}
			});
		}
		return searchCaseSensitiveCheckBox;
	}

	/**
	 * This method initializes searchMarkCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSearchMarkCheckBox() {
		if (searchMarkCheckBox == null) {
			searchMarkCheckBox = new JCheckBox();
			searchMarkCheckBox.setFont(new Font("Dialog", Font.PLAIN, 10));
			searchMarkCheckBox.setText("�}�[�N");
			
			searchMarkCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					getLogTableScrollPane().setMark(
							getSearchTextField().getText(),
							getSearchMarkCheckBox().isSelected(),
							getSearchRegExpCheckBox().isSelected(),
							getSearchCaseSensitiveCheckBox().isSelected());
					
					repaint();
				}
			});
		}
		return searchMarkCheckBox;
	}

	/**
	 * This method initializes searchCirculatingCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSearchCirculatingCheckBox() {
		if (searchCirculatingCheckBox == null) {
			searchCirculatingCheckBox = new JCheckBox();
			searchCirculatingCheckBox.setText("�z��");
			searchCirculatingCheckBox.setToolTipText("�擪/�����ɖ߂��Č������s��");
			searchCirculatingCheckBox.setFont(new Font("Dialog", Font.PLAIN, 10));
		}
		return searchCirculatingCheckBox;
	}
	
	private void setSearchResult(boolean found) {
		if (found) {
			statusMessageLabel.setText("");
		} else {
			statusMessageLabel.setText("�������[�h��������܂���");
		}
	}

	/**
	 * This method initializes openLogFileMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getOpenLogFileMenuItem() {
		if (openLogFileMenuItem == null) {
			openLogFileMenuItem = new JMenuItem();
			openLogFileMenuItem.setText("���O�t�@�C�����J��(O)...");
			openLogFileMenuItem.setMnemonic(KeyEvent.VK_O);
			openLogFileMenuItem.setActionCommand("���O�t�@�C�����J��(O)...");
			
			openLogFileMenuItem.addActionListener(new ActionListener() {
				// ���O�t�@�C�����J��
				public void actionPerformed(ActionEvent e) {
					File selectedFile = LogFileDialogHelper.
						getSelectedLogFile(ServletLogViewerFrame.this);
					
					if (selectedFile != null) {
						logFile = selectedFile;
						openLogFile();
					}
				}
			});
		}
		return openLogFileMenuItem;
	}

	/**
	 * This method initializes reopenLogFileMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getReopenLogFileMenuItem() {
		if (reopenLogFileMenuItem == null) {
			reopenLogFileMenuItem = new JMenuItem();
			reopenLogFileMenuItem.setText("���O�t�@�C�����J������(R)");
			reopenLogFileMenuItem.setMnemonic(KeyEvent.VK_R);
			
			reopenLogFileMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (logFile != null) {
						openLogFile();
					}
				}
			});
		}
		return reopenLogFileMenuItem;
	}

	/**
	 * This method initializes exportLogMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExportLogMenuItem() {
		if (exportLogMenuItem == null) {
			exportLogMenuItem = new JMenuItem();
			exportLogMenuItem.setText("���O�̃G�N�X�|�[�g(X)...");
			exportLogMenuItem.setMnemonic(KeyEvent.VK_X);
			exportLogMenuItem.setToolTipText("�\�����̃��O���t�@�C���ɃG�N�X�|�[�g���܂�");
			
			exportLogMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// �t�@�C���_�C�A���O��\�����A���݂̃e�[�u���̓��e��ۑ�����B
					LogFileDialogHelper.saveLogFile(
							ServletLogViewerFrame.this, 
							getLogTableModel());
				}
			});
		}
		return exportLogMenuItem;
	}

	/**
	 * This method initializes exitMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("�I��(E)");
			exitMenuItem.setMnemonic(KeyEvent.VK_E);
			
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes columnConfigMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getColumnConfigMenuItem() {
		if (columnConfigMenuItem == null) {
			columnConfigMenuItem = new JMenuItem();
			columnConfigMenuItem.setText("��̐ݒ�(C)...");
			columnConfigMenuItem.setMnemonic(KeyEvent.VK_C);
			
			columnConfigMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO ��̐ݒ�
					showMessageDialog(null,
							"�܂��ł��Ă��܂���",
							"",
							INFORMATION_MESSAGE);
				}
			});
		}
		return columnConfigMenuItem;
	}

	/**
	 * This method initializes statusBarCheckBoxMenuItem	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getStatusBarCheckBoxMenuItem() {
		if (statusBarCheckBoxMenuItem == null) {
			statusBarCheckBoxMenuItem = new JCheckBoxMenuItem();
			statusBarCheckBoxMenuItem.setText("�X�e�[�^�X�o�[�̕\��");
			
			statusBarCheckBoxMenuItem.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					getStatusToolBar().setVisible(statusBarCheckBoxMenuItem.isSelected());
				}
			});
		}
		return statusBarCheckBoxMenuItem;
	}

	/**
	 * This method initializes autoScrollCheckBoxMenuItem	
	 * 	
	 * @return CheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getAutoScrollCheckBoxMenuItem() {
		if (autoScrollCheckBoxMenuItem == null) {
			autoScrollCheckBoxMenuItem = new JCheckBoxMenuItem();
			autoScrollCheckBoxMenuItem.setText("�����X�N���[��");
		}
		return autoScrollCheckBoxMenuItem;
	}

	/**
	 * This method initializes viewerPreferencesMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getViewerPreferencesMenuItem() {
		if (viewerPreferencesMenuItem == null) {
			viewerPreferencesMenuItem = new JMenuItem();
			viewerPreferencesMenuItem.setText("�r���[�A�̐ݒ�...");
			
			viewerPreferencesMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					JDialog dialog = new ViewerPreferencesDialog();
					dialog.setLocationRelativeTo(ServletLogViewerFrame.this);
					dialog.setVisible(true);
				}
			});
		}
		return viewerPreferencesMenuItem;
	}

	/**
	 * This method initializes loggerConfigMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getLoggerConfigMenuItem() {
		if (loggerConfigMenuItem == null) {
			loggerConfigMenuItem = new JMenuItem();
			loggerConfigMenuItem.setText("���K�[�̐ݒ�...");
			
			loggerConfigMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					try {
						// �N���X�p�X��tools.jar�����邩�`�F�b�N����
						Class.forName("com.sun.tools.attach.VirtualMachine");
					} catch (ClassNotFoundException ex) {
						JOptionPane.showMessageDialog(
								null,
								"tools.jar�̃N���X��������܂���B�N���X�p�X�̐ݒ���������Ă��������B",
								"",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					JavaProcessDialog dialog = new JavaProcessDialog(ServletLogViewerFrame.this);
					
					if (dialog.isLoadSuccess()) {
						dialog.setLocationRelativeTo(ServletLogViewerFrame.this);
						dialog.setVisible(true);
					} else {
						dialog.dispose();
					}
				}
			});
		}
		return loggerConfigMenuItem;
	}

	/**
	 * This method initializes versionInfoMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getVersionInfoMenuItem() {
		if (versionInfoMenuItem == null) {
			versionInfoMenuItem = new JMenuItem();
			versionInfoMenuItem.setText("�o�[�W�������");
			
			versionInfoMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog dialog = new VersionInfoDialog(ServletLogViewerFrame.this);
					dialog.setLocationRelativeTo(ServletLogViewerFrame.this);
					dialog.setVisible(true);
				}
			});
		}
		return versionInfoMenuItem;
	}

	public static void main(String args[]) {
		
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		
		final File initLogFile;
		if (args.length >= 1) {
			initLogFile = new File(args[0]);
		} else {
			initLogFile = null;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ServletLogViewerFrame frame =
					new ServletLogViewerFrame(initLogFile);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
	
	/**
	 * This is the default constructor
	 */
	public ServletLogViewerFrame(File initLogFile) {
		super();
		initialize();
		restorePreviousState();
		openInitLogFile(initLogFile);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(806, 408);
		this.setJMenuBar(getJJMenuBar());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("mikaboshi Servlet Monitor");
		
		((CardLayout) getLogTablePanel().getLayout()).show(
				getLogTablePanel(), getFileOpenPanel().getName());
		
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				// �I�����ɐݒ��ۑ�����
				
				ViewerConfig config = ViewerConfig.getInstance();
				ViewerConfig.LogTable logTable = config.getLogTable();
				ViewerConfig.Frame frame = config.getFrame();
//				
//				// ��̕\�����
//				logTable.setTimestamp(getTimeCheckBoxMenuItem().isSelected());
//				logTable.setLogType(getLogTypeCheckBoxMenuItem().isSelected());
//				logTable.setThreadName(getThreadNameCheckBoxMenuItem().isSelected());
//				logTable.setConnectionId(getConnectionIdCheckBoxMenuItem().isSelected());
//				logTable.setStatementId(getStatementIdCheckBoxMenuItem().isSelected());
//				logTable.setSql(getSqlCheckBoxMenuItem().isSelected());
//				logTable.setAffectedRows(getAffectedRowsCheckBoxMenuItem().isSelected());
//				logTable.setElapsedTime(getElapsedTimeCheckBoxMenuItem().isSelected());
//				logTable.setResult(getResultCheckBoxMenuItem().isSelected());
				
				// �E�B���h�E�T�C�Y
				if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
					frame.setMaximized(true);
				} else {
					frame.setMaximized(false);
					frame.setDimension(getSize());
				}
				
				// �����X�N���[�����[�h
				logTable.setAutoScroll(getAutoScrollCheckBoxMenuItem().isSelected());
				
				// �X�e�[�^�X�o�[
				frame.setStatusBar(getStatusToolBar().isVisible());
				
				config.store();
			}
		});
		
		new DropTarget(this, new LogFileDropTargetAdapter());
	}
	
	/**
	 * �O��N�����̏�Ԃ𕜌�����
	 */
	private void restorePreviousState() {
		
		ViewerConfig config = ViewerConfig.getInstance();
		
		// �O��J�������O�t�@�C���̕���
		if (config.getLogFile().getPath() != null) {
			this.logFile = new File(config.getLogFile().getPath());
			
			if (!this.logFile.exists() || !this.logFile.isFile()) {
				this.logFile = null;
			}
		}
		
		System.out.println("ServletLogViewerFrame.restorePreviousState() : " + config);
		
		updateReopenComponents();
		
		// ��̕\����Ԃ̕���
		ViewerConfig.LogTable logTable = config.getLogTable();
//		getTimeCheckBoxMenuItem().setState(logTable.isTimestamp());
//		getLogTypeCheckBoxMenuItem().setState(logTable.isLogType());
//		getThreadNameCheckBoxMenuItem().setState(logTable.isThreadName());
//		getConnectionIdCheckBoxMenuItem().setState(logTable.isConnectionId());
//		getStatementIdCheckBoxMenuItem().setState(logTable.isStatementId());
//		getSqlCheckBoxMenuItem().setState(logTable.isSql());
//		getElapsedTimeCheckBoxMenuItem().setState(logTable.isElapsedTime());
//		getAffectedRowsCheckBoxMenuItem().setState(logTable.isAffectedRows());
//		getResultCheckBoxMenuItem().setState(logTable.isResult());
//		
//		refreshColumns();
		
		// �E�B���h�E�̑傫���̕���
		ViewerConfig.Frame frame = config.getFrame();
		
		if (frame.isMaximized()) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			this.setSize(frame.getDimension());
		}
		
//		// �t�B���^�̕���
//		getLogTableModel().updateFilter(getLogTableFilter());
//		
		// �����X�N���[�����[�h�̕���
		getAutoScrollCheckBoxMenuItem().setSelected(
				logTable.isAutoScroll());
		
		// �X�e�[�^�X�o�[�̕���
		getStatusBarCheckBoxMenuItem().setSelected(frame.isStatusBar());
		getStatusToolBar().setVisible(frame.isStatusBar());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(10);
			borderLayout.setVgap(0);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout);
			jContentPane.add(getButtonsPanel(), BorderLayout.NORTH);
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
			jContentPane.add(getStatusToolBar(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	private LogTableScrollPane getLogTableScrollPane() {
		if (logTableScrollPane == null) {
			logTableScrollPane = new LogTableScrollPane(this);
			logTableScrollPane.setName("logTableScrollPane");
		}
		return logTableScrollPane;
	}
	
	/**
	 * ���O�t�@�C�����J���B
	 */
	private void openLogFile() {
		
		// �����̍s���N���A���� 
		getLogTableModel().reset();
		
		// �v���r���[���폜
		selectLogEntry(null);
		
		// �t�B���^�[�R���{�����Z�b�g
		FilterComboItems.getInstance().reset();
		
		this.logAccessor = new LogFileAccessor(
				logFile, 
				ViewerConfig.getInstance().getLogFile().getCharSet());
		
		// �擪����ǂݍ��ނ��ǂ����m�F
		boolean loadOnOpen = showConfirmDialog(
				null,
				"���O�t�@�C���̐擪����ǂݍ��݂܂����H\r\n" + this.logFile.getAbsolutePath(),
	            "",
	            YES_NO_OPTION) == YES_OPTION;

		getReadToggleButton().setSelected(true);
		getSuspendToggleButton().setSelected(false);
		
		// ���O�e�[�u���̕\��
		((CardLayout) getLogTablePanel().getLayout()).show(
				getLogTablePanel(), getLogTableScrollPane().getName());

		
		// ���łɏo�͂���Ă��郍�O��ǂݍ���
		while (true) {
			try {
				LogEntry entry = this.logAccessor.readNextLog();
				
				if (entry == null) {
					this.logAccessor.unlock();
					break;
				}
				
				if (loadOnOpen) {
					getLogTableModel().addLogEntry(entry);
				}
				
			} catch (IOException e) {
				systemLogger.error("Loading logfile failed.", e);
				
				showMessageDialog(null,
						M17N.get("message.unexpected_error", e.getMessage()),
						"",
						ERROR_MESSAGE);
				
				this.logAccessor.close();
				this.logAccessor = null;
				break;
			}
		}
		
		getLogTableModel().fireTableDataChanged();
		getLogTableScrollPane().updateColumnWidth();
		
		ViewerConfig.getInstance().getLogFile().setPath(this.logFile.getAbsolutePath());
		updateReopenComponents();
		
		if (this.logAccessor == null) {
			// �ŏ��̓ǂݍ��ݎ��ɃG���[���������ꍇ�͍X�V���Ȃ�
			return;
		}
		
		updateStatusBar();
		
		// �X�V���ꂽ���O��ǂݍ���
		new Thread() {
			public void run() {
				LogFileAccessor ownAccessor = logAccessor;
				
				while (true) {
					if (ownAccessor != logAccessor) {
						// ���O�t�@�C�����ς����/�J�������ꂽ�ꍇ�A���̃X���b�h�͏I��
						break;
					}
					
					LogEntry entry = null;
					
					try {
						entry = logAccessor.readNextLog();
						
					} catch (IOException e) {
						systemLogger.error("Loading log file failed.", e);
						
						showMessageDialog(null,
								M17N.get("message.unexpected_error", e.getMessage()),
								"",
								ERROR_MESSAGE);
						
						logAccessor.unlock();
						break;
					}
					
					if ( entry != null && getReadToggleButton().isSelected() ) {
						getLogTableModel().addLogEntryWithFireUpdate(entry);
						
						// �ŉ��s��\������
						if (getAutoScrollCheckBoxMenuItem().isSelected()) {
							getLogTableScrollPane().getVerticalScrollBar().
								setValue(getLogTableScrollPane().
								getVerticalScrollBar().getMaximum());
						}
						
						updateStatusBar();
						
						continue;
					} else {
						logAccessor.unlock();
					}
					
					try {
						Thread.sleep(
								ViewerConfig.getInstance().getLogFile().getReadInterval());
						
					} catch (InterruptedException e) {
						systemLogger.error(e);
					}
				}
				
				ownAccessor.close();
			}
		}.start();
	}
	
	/**
	 * �R�}���h���C�������ŗ^����ꂽ�t�@�C�����J��
	 */
	private void openInitLogFile(File initLogFile) {
		if (initLogFile == null) {
			// �������Ȃ��ꍇ
			return;
		}
		
		if ( !initLogFile.exists() || !initLogFile.isFile() ) {
			// �����̃t�@�C�������݂��Ȃ��ꍇ
			systemLogger.warn("Log file does not exist."
				+ initLogFile.getAbsolutePath());
		}
		
		this.logFile = initLogFile;
		updateReopenComponents();
		openLogFile();
	}
	
	/**
	 * ���O�t�@�C�����J�������Ɋւ���R���|�[�l���g�̕\���ݒ�
	 */
	private void updateReopenComponents() {
		
		if (this.reopenButton == null || this.reopenLogFileMenuItem == null) {
			return;
		}
		
		if (this.logFile != null) {
			this.reopenButton.setEnabled(true);
			this.reopenButton.setToolTipText(
					"�J������" + " " +
					this.logFile.getAbsolutePath());
			this.reopenLogFileMenuItem.setEnabled(true);
			this.reopenLogFileMenuItem.setToolTipText(this.logFile.getAbsolutePath());
		} else {
			this.reopenButton.setEnabled(false);
			this.reopenButton.setToolTipText(
					"�J������");
			this.reopenLogFileMenuItem.setEnabled(false);
			this.reopenLogFileMenuItem.setToolTipText("");
		}
	}
	
	/**
	 * �X�e�[�^�X�o�[�̕\�����e���X�V����B
	 */
	void updateStatusBar() {
		if ( !getStatusToolBar().isVisible() ) {
			return;
		}
		
		this.lastLogDateTimeLabel.setText(
				getLogTableModel().getLastLogDateTime());
		this.selectedRowsCountLabel.setText(
				String.valueOf(getLogTableScrollPane().getSelectedRowsCount()));
		this.visibleRowsCountLabel.setText(
				String.valueOf(getLogTableModel().getRowCount()));
		this.allRowsCountLabel.setText(
				String.valueOf(getLogTableModel().getAllRowsCount()));
	}
	
	private LogTableModel getLogTableModel() {
		return getLogTableScrollPane().getLogTableModel();
	}

	/**
	 * ���O�t�@�C�����G�N�X�v���[������h���b�O���h���b�v�ŊJ����悤�ɂ���
	 */
	private class LogFileDropTargetAdapter extends DropTargetAdapter {

		@SuppressWarnings("unchecked")
		public void drop(DropTargetDropEvent dtde) {
			Transferable transfer = dtde.getTransferable();

			if (!transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				return;
			}
			
			dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			
			try {
				List<File> fileList = (List<File>) 
					transfer.getTransferData(DataFlavor.javaFileListFlavor);
				
				if (fileList != null && !fileList.isEmpty()) {
					logFile = fileList.get(0);
					openLogFile();
				}
				
			} catch (Exception e) {
				systemLogger.error(
						"Loading log file vie D&D failed.", e);
			}
		}
		
	}
	
	/**
	 * ���O�e�[�u�����烍�O��I��
	 * @param logEntry
	 */
	protected void selectLogEntry(LogEntry logEntry) {
		getLogDetailTabbedPane().setLogEntry(logEntry);
	}
	
	/**
	 * �t�B���^���X�V���A���O�e�[�u�����ĕ`�悷��B
	 */
	public void resetFilter() {
		getLogTableModel().resetFilter();
		updateStatusBar();
		repaint();
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
