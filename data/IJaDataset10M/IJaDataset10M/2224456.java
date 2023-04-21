package net.narusas.aceauction.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.FileUploadListener;
import net.narusas.aceauction.data.FileUploaderBG;
import net.narusas.aceauction.data.builder.Builder;
import net.narusas.aceauction.fetchers.����������򰡼�Fetcher;
import net.narusas.aceauction.fetchers.�������Ȳ���缭Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.gamjung.GamjungController;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.aceauction.pdf.gamjung.GamjungUI;
import net.narusas.aceauction.pdf.gamjung.GamjungParser.Group;
import net.narusas.aceauction.util.DateUtil;
import net.narusas.util.lang.NFile;

public class Controller {

	public static boolean updating = false;

	static Logger logger = Logger.getLogger("log");

	private Properties cfg;

	private ConsoleFrame f;

	private BeansTableModel infoTableModel;

	private Thread workThread;

	private ����ListModel ����ListModeInstance;

	private ���ListModel ���ListModelInstance;

	private ����ListModel ����ListModeInstance;

	private ��������ListModel ��������ListModelInstance;
	private ���ListModel ���ListModeInstance;

	public void setFrame(ConsoleFrame fr) {

		loadConfig();

		this.f = fr;

		ProgressBar.setProgress(new SwingProgress(f));
		Alert.setAlert(new SwingAlert(f));
		f.get����List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get����List().setModel(get����ListModel());

		f.get����List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				����selected(����.get(f.get����List().getSelectedIndex()));
			}
		});

		f.get���List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get���List().setModel(get���ListModel());
		f.get����List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				����selected(f.get����List().getSelectedIndex());
			}

		});
		f.get����List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get����List().setModel(get����ListModel());
		f.get���List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				���selected(f.get���List().getSelectedIndex());
			}
		});

		f.get��������List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get��������List().setModel(get��������ListModel());
		f.get����List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				����selected(f.get����List().getSelectedIndex());
			}
		});

		f.get��������List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				��������selected(f.get��������List().getSelectedIndex());
			}
		});

		f.get���List().setModel(get���ListModel());
		f.get���List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get���List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				logger.info("���List valueChanged");

				try {
					���selected(f.get���List().getSelectedIndex());
				} catch (Exception e1) {
					logger.info(e1.getMessage());
				}
			}
		});
		f.getInfoTable().setModel(getInfoTableModel());

		f.get�Button().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				�clicked();
			}
		});

		f.get�2Button().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				�2clicked();
			}

		});

		f.get�����������缭Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				�����������缭clicked();
			}
		});
		f.get�ε���ǥ�ø��Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				�ε���ǥ�ø��clicked();
			}
		});

		f.get�Ű����Ǹ���Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				�Ű����Ǹ���clicked();
			}
		});

		f.get�����򰡼�Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				�����򰡼�clicked();
			}
		});

		f.getWorkButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Long[] eventNos = null;

				int[] indexs = f.get���List().getSelectedIndices();
				if (indexs == null || indexs.length == 0) {
					eventNos = null;
				} else {
					List<Long> res = new LinkedList<Long>();
					for (int i = 0; i < indexs.length; i++) {
						��� s = (���) ���ListModeInstance.get(indexs[i]);
						res.add(s.get��ǹ�ȣ());
					}
					eventNos = res.toArray(new Long[res.size()]);
				}

				work(f.get����List().getSelectedIndex(), toInt(f.getStartYearTextField().getText()), toInt(f
						.getStartMonthTextField().getText()), toInt(f.getStartDayTextField().getText()), toInt(f
						.getEndYearTextField().getText()), toInt(f.getEndMonthTextField().getText()), toInt(f
						.getEndDayTextField().getText()), eventNos

				);

			}
		});

		if (new File("admin.txt").exists() == false) {
			f.getWorkButton().setEnabled(false);
			f.getAllWorkButton().setEnabled(false);
		}

		Date start = new Date(System.currentTimeMillis());
		f.getStartYearTextField().setText(String.valueOf(start.getYear() + 1900));
		f.getStartMonthTextField().setText(String.valueOf(start.getMonth() + 1));
		f.getStartDayTextField().setText(String.valueOf(start.getDate()));

		Date end = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 20));
		f.getEndYearTextField().setText(String.valueOf(end.getYear() + 1900));
		f.getEndMonthTextField().setText(String.valueOf(end.getMonth() + 1));
		f.getEndDayTextField().setText(String.valueOf(end.getDate()));

		f.bgWorkLabel.setText("----");

		FileUploaderBG.getInstance().addListener(new FileUploadListener() {
			public void update(int count, int remains, String currentWork) {
				if (f != null && f.bgWorkLabel != null) {
					f.bgWorkLabel.setText("���� �۾� :" + remains + " �۾� ī��Ʈ:" + count + " �����۾�:" + currentWork);
				}
			}
		});
		FileUploaderBG.getInstance().start();

		f.getUndoButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				new Command(new UndoTask(f, ����ListModeInstance), 100).start();
			}
		});

		f.getDeleteAllButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				try {
					DB db = new DB();
					Connection conn = db.dbConnect();
					Statement stmt = conn.createStatement();

					stmt.executeUpdate("DELETE FROM ac_charge WHERE no>=0;");
					stmt.executeUpdate("DELETE FROM ac_event WHERE no>=0;");
					stmt.executeUpdate("DELETE FROM ac_goods WHERE id>=0;");

					stmt.executeUpdate("DELETE FROM ac_appoint_statement WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_bld_statement WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_goods_statement WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_land_right_statement WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_land_statement WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_exclusion WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_participant WHERE no>=0;");
					stmt.executeUpdate("DELETE FROM ac_attested_statement WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_attested WHERE id>=0;");
					stmt.executeUpdate("DELETE FROM ac_goods_building WHERE id>=0;");
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		f.getAllWorkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					public void run() {
						for (int i = 0; i < ����.size(); i++) {
							work(i, toInt(f.getStartYearTextField().getText()), toInt(f.getStartMonthTextField()
									.getText()), toInt(f.getStartDayTextField().getText()), toInt(f
									.getEndYearTextField().getText()), toInt(f.getEndMonthTextField().getText()),
									toInt(f.getEndDayTextField().getText()), null

							);
							if (workThread != null) {
								try {
									workThread.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						}
					}
				}.start();

			}
		});
	}

	public void ����selected(���� court) {
		Controller.updating = true;
		get����ListModel().clear();
		get���ListModel().clear();
		get����ListModel().clear();
		get���ListModel().clear();
		get��������ListModel().clear();
		getInfoTableModel().clear();

		Command cmd = new Command(new ����FetchTask(court, get����ListModel()), 6);
		cmd.start();
	}

	private BeansTableModel getInfoTableModel() {
		if (infoTableModel == null) {
			infoTableModel = new BeansTableModel();
		}
		return infoTableModel;
	}

	private ���ListModel get���ListModel() {
		if (this.���ListModelInstance == null) {
			���ListModelInstance = new ���ListModel();
		}
		return ���ListModelInstance;
	}

	private ��������ListModel get��������ListModel() {
		if (this.��������ListModelInstance == null) {
			��������ListModelInstance = new ��������ListModel();
		}
		return ��������ListModelInstance;
	}

	private void loadConfig() {
		cfg = new Properties();
		try {
			cfg.load(new FileInputStream(new File("config.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openGamjungUI(��� s, File file) {
		GamjungUI ui = new GamjungUI();
		GamjungController c = new GamjungController(ui);
		GamjungParser parser = new GamjungParser();
		try {
			List<Group> groups = parser.parse(file);
			c.setGamjungs(s, groups, parser.getSrc(), parser.getDate());
			ui.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openPDF(File file) {
		if (file == null) {
			return;
		}
		try {
			// File converted = ���ε���ں���.convert(file.getAbsolutePath());

			logger.info("Open PDF " + file.getAbsolutePath());
			Process ps = Runtime.getRuntime().exec(cfg.getProperty("pdfreader") + " " + file.getAbsolutePath());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showBrowser(File file) throws MalformedURLException, IOException {
		Process ps = Runtime.getRuntime().exec("c:\\Program Files\\Internet Explorer\\IEXPLORE.EXE " + file.toURL());

	}

	private void ����selected(int index) {
		updating = true;

		get���ListModel().clear();
		get����ListModel().clear();
		get���ListModel().clear();
		get��������ListModel().clear();
		getInfoTableModel().clear();

		���� charge = ����ListModeInstance.get����(index);
		Date date = charge.get�Ű�����();
		Pattern p = Pattern.compile("(\\d{4,4})[.-]+\\s*(\\d+)[.-]+\\s*(\\d+)");
		Matcher m = p.matcher(date.toString());
		if (m.find()) {
			String year = m.group(1);
			String month = m.group(2);
			String day = m.group(3);

			f.getStartYearTextField().setText(year);
			f.getStartMonthTextField().setText(month);
			f.getStartDayTextField().setText(day);

			f.getEndYearTextField().setText(year);
			f.getEndMonthTextField().setText(month);
			f.getEndDayTextField().setText(day);
		}

		Command cmd = new Command(new ���FetchTask(get���ListModel(), ����ListModeInstance.get����(index),
				getInfoTableModel()), 20);
		cmd.start();

	}

	private void �2clicked() {
		int index = f.get����List().getSelectedIndex();
		if (index < 0 || index >= ����ListModeInstance.size()) {
			return;
		}
		���� s = (����) ����ListModeInstance.getElementAt(index);
		new Command(new �Task(s, cfg), 10).start();
	}

	protected void work(final int selecteCourt, final int sYear, final int sMonth, final int sDay, final int eYear,
			final int eMonth, final int eDay, final Long[] eventNos) {
		workThread = new Thread() {
			Builder builder;

			public void run() {
				if (selecteCourt == -1 || selecteCourt >= ����.size()) {
					return;
				}
				���� court = ����.get(selecteCourt);

				Date s = new Date(sYear - 1900, sMonth - 1, sDay);
				Date end = new Date(eYear - 1900, eMonth - 1, eDay);
				logger.info("�۾� ��� �Ⱓ�� ������=" + DateUtil.dateString(s) + ", ������=" + DateUtil.dateString(end));
				if (eventNos != null) {
					String temp = "";
					for (int i = 0; i < eventNos.length; i++) {
						temp += eventNos[i] + ",";
					}
					logger.info("�۾���� ��ǹ�ȣ:" + temp);
				} else {
					logger.info("�ش� ������ ��� ����� ������� �۾��մϴ�. ");
				}

				final SwingBuildProgressListener sbpl = new SwingBuildProgressListener(f);
				sbpl.setResizable(true);
				sbpl.setLocationRelativeTo(f);
				builder = new Builder(court, sbpl, s, end, eventNos);
				try {
					new Thread() {
						@Override
						public void run() {
							sbpl.setModal(true);
							sbpl.setVisible(true);
							cancelWork();
						}

					}.start();
					builder.build();
				} catch (Exception e) {
					e.printStackTrace();
					Alert.getInstance().alert(e.getMessage(), e);
				}
				sbpl.setVisible(false);
			}

			private void cancelWork() {
				this.stop();
			}
		};// .start();
		workThread.start();
	}

	protected void �����򰡼�clicked() {
		int index = f.get���List().getSelectedIndex();
		if (index < 0 || index >= ���ListModeInstance.size()) {
			return;
		}
		��� s = (���) ���ListModeInstance.getElementAt(index);
		����������򰡼�Fetcher f = new ����������򰡼�Fetcher();
		try {
			byte[] pdf = f.fetch(s.get�����򰡼�(), true);
			if (pdf == null) {
				Alert.getInstance().alert("�����򰡼��� ���� ����Դϴ�");
				return;
			}
			if (pdf.length == 0) {
				GamjungUI ui = new GamjungUI();
				GamjungController c = new GamjungController(ui);
				c.setGamjungs(s, null, null, null);
				ui.setVisible(true);
				return;
			}
			File file = File.createTempFile("�����򰡼�", ".pdf");
			NFile.write(file, pdf);
			openPDF(file);
			openGamjungUI(s, file);

		} catch (Exception e) {
			Alert.getInstance().alert(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	protected void �clicked() {
		int index = f.get���List().getSelectedIndex();
		if (index < 0 || index >= ���ListModeInstance.size()) {
			return;
		}
		��� s = (���) ���ListModeInstance.getElementAt(index);
		new Command(new �Task(s, cfg), 10).start();
	}

	protected void �Ű����Ǹ���clicked() {

		int index = f.get����List().getSelectedIndex();
		if (index < 0 || index >= ����ListModeInstance.size()) {
			return;
		}
		���� m = (����) ����ListModeInstance.getElementAt(index);
		// System.out.println("M:"+m.get�Ű����Ǹ���html());
		if (m.get�Ű����Ǹ���html() == null) {
			return;
		}

		try {
			File file = File.createTempFile("�Ű����Ǹ���", ".html");
			NFile.write(file, m.get�Ű����Ǹ���html(), "euc-kr");
			showBrowser(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void ���selected(int index) {
		logger.info("���selected");
		updating = true;
		// get���ListModel().clear();
		getInfoTableModel().clear();

		���� ���� = (����) get����ListModel().getElementAt(f.get����List().getSelectedIndex());
		String ��� = (String) get��������ListModel().getElementAt(f.get��������List().getSelectedIndex());
		if (f.get���List().getSelectedIndex() == -1) {
			return;
		}
		String ��ϼ��� = (String) get���ListModel().getElementAt(f.get���List().getSelectedIndex());

		new Command(new ���Task(����, ���, ��ϼ���, getInfoTableModel(), get���ListModel()), 10).start();
	}

	protected void ����selected(int index) {
		updating = true;
		get���ListModel().clear();
		get��������ListModel().clear();
		getInfoTableModel().clear();

		new Command(new ����Task((����) get����ListModel().getElementAt(index), getInfoTableModel(), get��������ListModel()), 20)
				.start();
	}

	protected void ��������selected(int index) {
		updating = true;
		get���ListModel().clear();
		getInfoTableModel().clear();

		���� ���� = (����) get����ListModel().getElementAt(f.get����List().getSelectedIndex());
		String ��� = (String) get��������ListModel().getElementAt(index);

		new Command(new ��������Task(����, ���, getInfoTableModel(), get���ListModel()), 10).start();
	}

	protected void �ε���ǥ�ø��clicked() {
		int index = f.get���List().getSelectedIndex();
		if (index < 0 || index >= ���ListModeInstance.size()) {
			return;
		}
		��� s = (���) ���ListModeInstance.getElementAt(index);
		�������Ȳ���缭Fetcher f = new �������Ȳ���缭Fetcher();
		try {
			String[] htmls = f.fetchAll(s, s.get��Ȳ���缭());
			File file = File.createTempFile("�ε���ǥ�ø��", ".html");
			NFile.write(file, htmls[1], "euc-kr");
			showBrowser(file);

		} catch (Exception e) {
			Alert.getInstance().alert(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	protected void ���selected(int index) {
		updating = true;
		get����ListModel().clear();
		get���ListModel().clear();
		get��������ListModel().clear();
		getInfoTableModel().clear();
		new Command(new ���Task(get����ListModel(), getInfoTableModel(), (���) ���ListModeInstance.getElementAt(index),
				get����ListModel()), 20).start();
	}

	protected void �����������缭clicked() {
		int index = f.get���List().getSelectedIndex();
		if (index < 0 || index >= ���ListModeInstance.size()) {
			return;
		}
		��� s = (���) ���ListModeInstance.getElementAt(index);
		�������Ȳ���缭Fetcher f = new �������Ȳ���缭Fetcher();
		try {
			String[] htmls = f.fetchAll(s, s.get��Ȳ���缭());
			File file = File.createTempFile("�����������缭", ".html");
			NFile.write(file, htmls[0], "euc-kr");
			showBrowser(file);

		} catch (Exception e) {
			Alert.getInstance().alert(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	����ListModel get����ListModel() {
		if (this.����ListModeInstance == null) {
			����ListModeInstance = new ����ListModel();
		}
		return ����ListModeInstance;
	}

	����ListModel get����ListModel() {
		if (this.����ListModeInstance == null) {
			����ListModeInstance = new ����ListModel();
		}
		return ����ListModeInstance;
	}

	���ListModel get���ListModel() {
		if (this.���ListModeInstance == null) {
			���ListModeInstance = new ���ListModel();
		}
		return ���ListModeInstance;
	}

	int toInt(String src) {
		return Integer.parseInt(src);
	}
}
