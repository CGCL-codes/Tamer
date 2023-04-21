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
import net.narusas.aceauction.fetchers.대법원감정평가서Fetcher;
import net.narusas.aceauction.fetchers.대법원현황조사서Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;
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

	private 담당계ListModel 담당계ListModeInstance;

	private 목록ListModel 목록ListModelInstance;

	private 물건ListModel 물건ListModeInstance;

	private 물건정보ListModel 물건정보ListModelInstance;
	private 사건ListModel 사건ListModeInstance;

	public void setFrame(ConsoleFrame fr) {

		loadConfig();

		this.f = fr;

		ProgressBar.setProgress(new SwingProgress(f));
		Alert.setAlert(new SwingAlert(f));
		f.get담당계List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get담당계List().setModel(get담당계ListModel());

		f.get법원List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				법원selected(법원.get(f.get법원List().getSelectedIndex()));
			}
		});

		f.get사건List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get사건List().setModel(get사건ListModel());
		f.get담당계List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				담당계selected(f.get담당계List().getSelectedIndex());
			}

		});
		f.get물건List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get물건List().setModel(get물건ListModel());
		f.get사건List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				사건selected(f.get사건List().getSelectedIndex());
			}
		});

		f.get물건정보List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get물건정보List().setModel(get물건정보ListModel());
		f.get물건List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				물건selected(f.get물건List().getSelectedIndex());
			}
		});

		f.get물건정보List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				물건정보selected(f.get물건정보List().getSelectedIndex());
			}
		});

		f.get목록List().setModel(get목록ListModel());
		f.get목록List().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		f.get목록List().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (updating || e.getValueIsAdjusting()) {
					return;
				}
				logger.info("목록List valueChanged");

				try {
					목록selected(f.get목록List().getSelectedIndex());
				} catch (Exception e1) {
					logger.info(e1.getMessage());
				}
			}
		});
		f.getInfoTable().setModel(getInfoTableModel());

		f.get등본Button().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				등본clicked();
			}
		});

		f.get등본2Button().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				등본2clicked();
			}

		});

		f.get점유관계조사서Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				점유관계조사서clicked();
			}
		});
		f.get부동산표시목록Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				부동산표시목록clicked();
			}
		});

		f.get매각물건명세서Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				매각물건명세서clicked();
			}
		});

		f.get감정평가서Button().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				감정평가서clicked();
			}
		});

		f.getWorkButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Long[] eventNos = null;

				int[] indexs = f.get사건List().getSelectedIndices();
				if (indexs == null || indexs.length == 0) {
					eventNos = null;
				} else {
					List<Long> res = new LinkedList<Long>();
					for (int i = 0; i < indexs.length; i++) {
						사건 s = (사건) 사건ListModeInstance.get(indexs[i]);
						res.add(s.get사건번호());
					}
					eventNos = res.toArray(new Long[res.size()]);
				}

				work(f.get법원List().getSelectedIndex(), toInt(f.getStartYearTextField().getText()), toInt(f
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
					f.bgWorkLabel.setText("남은 작업 :" + remains + " 작업 카운트:" + count + " 현재작업:" + currentWork);
				}
			}
		});
		FileUploaderBG.getInstance().start();

		f.getUndoButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				new Command(new UndoTask(f, 담당계ListModeInstance), 100).start();
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
						for (int i = 0; i < 법원.size(); i++) {
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

	public void 법원selected(법원 court) {
		Controller.updating = true;
		get담당계ListModel().clear();
		get사건ListModel().clear();
		get물건ListModel().clear();
		get목록ListModel().clear();
		get물건정보ListModel().clear();
		getInfoTableModel().clear();

		Command cmd = new Command(new 담당계FetchTask(court, get담당계ListModel()), 6);
		cmd.start();
	}

	private BeansTableModel getInfoTableModel() {
		if (infoTableModel == null) {
			infoTableModel = new BeansTableModel();
		}
		return infoTableModel;
	}

	private 목록ListModel get목록ListModel() {
		if (this.목록ListModelInstance == null) {
			목록ListModelInstance = new 목록ListModel();
		}
		return 목록ListModelInstance;
	}

	private 물건정보ListModel get물건정보ListModel() {
		if (this.물건정보ListModelInstance == null) {
			물건정보ListModelInstance = new 물건정보ListModel();
		}
		return 물건정보ListModelInstance;
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

	private void openGamjungUI(사건 s, File file) {
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
			// File converted = 등기부등본날자변경.convert(file.getAbsolutePath());

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

	private void 담당계selected(int index) {
		updating = true;

		get사건ListModel().clear();
		get물건ListModel().clear();
		get목록ListModel().clear();
		get물건정보ListModel().clear();
		getInfoTableModel().clear();

		담당계 charge = 담당계ListModeInstance.get담당계(index);
		Date date = charge.get매각기일();
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

		Command cmd = new Command(new 사건FetchTask(get사건ListModel(), 담당계ListModeInstance.get담당계(index),
				getInfoTableModel()), 20);
		cmd.start();

	}

	private void 등본2clicked() {
		int index = f.get물건List().getSelectedIndex();
		if (index < 0 || index >= 물건ListModeInstance.size()) {
			return;
		}
		물건 s = (물건) 물건ListModeInstance.getElementAt(index);
		new Command(new 등본Task(s, cfg), 10).start();
	}

	protected void work(final int selecteCourt, final int sYear, final int sMonth, final int sDay, final int eYear,
			final int eMonth, final int eDay, final Long[] eventNos) {
		workThread = new Thread() {
			Builder builder;

			public void run() {
				if (selecteCourt == -1 || selecteCourt >= 법원.size()) {
					return;
				}
				법원 court = 법원.get(selecteCourt);

				Date s = new Date(sYear - 1900, sMonth - 1, sDay);
				Date end = new Date(eYear - 1900, eMonth - 1, eDay);
				logger.info("작업 대상 기간은 시작일=" + DateUtil.dateString(s) + ", 종료일=" + DateUtil.dateString(end));
				if (eventNos != null) {
					String temp = "";
					for (int i = 0; i < eventNos.length; i++) {
						temp += eventNos[i] + ",";
					}
					logger.info("작업대상 사건번호:" + temp);
				} else {
					logger.info("해당 담당계의 모든 사건을 대상으로 작업합니다. ");
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

	protected void 감정평가서clicked() {
		int index = f.get사건List().getSelectedIndex();
		if (index < 0 || index >= 사건ListModeInstance.size()) {
			return;
		}
		사건 s = (사건) 사건ListModeInstance.getElementAt(index);
		대법원감정평가서Fetcher f = new 대법원감정평가서Fetcher();
		try {
			byte[] pdf = f.fetch(s.get감정평가서(), true);
			if (pdf == null) {
				Alert.getInstance().alert("감정평가서가 없는 사건입니다");
				return;
			}
			if (pdf.length == 0) {
				GamjungUI ui = new GamjungUI();
				GamjungController c = new GamjungController(ui);
				c.setGamjungs(s, null, null, null);
				ui.setVisible(true);
				return;
			}
			File file = File.createTempFile("감정평가서", ".pdf");
			NFile.write(file, pdf);
			openPDF(file);
			openGamjungUI(s, file);

		} catch (Exception e) {
			Alert.getInstance().alert(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	protected void 등본clicked() {
		int index = f.get사건List().getSelectedIndex();
		if (index < 0 || index >= 사건ListModeInstance.size()) {
			return;
		}
		사건 s = (사건) 사건ListModeInstance.getElementAt(index);
		new Command(new 등본Task(s, cfg), 10).start();
	}

	protected void 매각물건명세서clicked() {

		int index = f.get물건List().getSelectedIndex();
		if (index < 0 || index >= 물건ListModeInstance.size()) {
			return;
		}
		물건 m = (물건) 물건ListModeInstance.getElementAt(index);
		// System.out.println("M:"+m.get매각물건명세서html());
		if (m.get매각물건명세서html() == null) {
			return;
		}

		try {
			File file = File.createTempFile("매각물건명세서", ".html");
			NFile.write(file, m.get매각물건명세서html(), "euc-kr");
			showBrowser(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void 목록selected(int index) {
		logger.info("목록selected");
		updating = true;
		// get목록ListModel().clear();
		getInfoTableModel().clear();

		물건 물건 = (물건) get물건ListModel().getElementAt(f.get물건List().getSelectedIndex());
		String 명령 = (String) get물건정보ListModel().getElementAt(f.get물건정보List().getSelectedIndex());
		if (f.get목록List().getSelectedIndex() == -1) {
			return;
		}
		String 목록선택 = (String) get목록ListModel().getElementAt(f.get목록List().getSelectedIndex());

		new Command(new 목록Task(물건, 명령, 목록선택, getInfoTableModel(), get목록ListModel()), 10).start();
	}

	protected void 물건selected(int index) {
		updating = true;
		get목록ListModel().clear();
		get물건정보ListModel().clear();
		getInfoTableModel().clear();

		new Command(new 물건Task((물건) get물건ListModel().getElementAt(index), getInfoTableModel(), get물건정보ListModel()), 20)
				.start();
	}

	protected void 물건정보selected(int index) {
		updating = true;
		get목록ListModel().clear();
		getInfoTableModel().clear();

		물건 물건 = (물건) get물건ListModel().getElementAt(f.get물건List().getSelectedIndex());
		String 명령 = (String) get물건정보ListModel().getElementAt(index);

		new Command(new 물건정보Task(물건, 명령, getInfoTableModel(), get목록ListModel()), 10).start();
	}

	protected void 부동산표시목록clicked() {
		int index = f.get사건List().getSelectedIndex();
		if (index < 0 || index >= 사건ListModeInstance.size()) {
			return;
		}
		사건 s = (사건) 사건ListModeInstance.getElementAt(index);
		대법원현황조사서Fetcher f = new 대법원현황조사서Fetcher();
		try {
			String[] htmls = f.fetchAll(s, s.get현황조사서());
			File file = File.createTempFile("부동산표시목록", ".html");
			NFile.write(file, htmls[1], "euc-kr");
			showBrowser(file);

		} catch (Exception e) {
			Alert.getInstance().alert(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	protected void 사건selected(int index) {
		updating = true;
		get물건ListModel().clear();
		get목록ListModel().clear();
		get물건정보ListModel().clear();
		getInfoTableModel().clear();
		new Command(new 사건Task(get물건ListModel(), getInfoTableModel(), (사건) 사건ListModeInstance.getElementAt(index),
				get물건ListModel()), 20).start();
	}

	protected void 점유관계조사서clicked() {
		int index = f.get사건List().getSelectedIndex();
		if (index < 0 || index >= 사건ListModeInstance.size()) {
			return;
		}
		사건 s = (사건) 사건ListModeInstance.getElementAt(index);
		대법원현황조사서Fetcher f = new 대법원현황조사서Fetcher();
		try {
			String[] htmls = f.fetchAll(s, s.get현황조사서());
			File file = File.createTempFile("점유관계조사서", ".html");
			NFile.write(file, htmls[0], "euc-kr");
			showBrowser(file);

		} catch (Exception e) {
			Alert.getInstance().alert(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	담당계ListModel get담당계ListModel() {
		if (this.담당계ListModeInstance == null) {
			담당계ListModeInstance = new 담당계ListModel();
		}
		return 담당계ListModeInstance;
	}

	물건ListModel get물건ListModel() {
		if (this.물건ListModeInstance == null) {
			물건ListModeInstance = new 물건ListModel();
		}
		return 물건ListModeInstance;
	}

	사건ListModel get사건ListModel() {
		if (this.사건ListModeInstance == null) {
			사건ListModeInstance = new 사건ListModel();
		}
		return 사건ListModeInstance;
	}

	int toInt(String src) {
		return Integer.parseInt(src);
	}
}
