package net.mikaboshi.servlet.monitor.viewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.mikaboshi.servlet.monitor.LogEntry;

public final class LogTableColumn {
	
	private LogTableColumn() {}
	
	private static final LogTableColumn INSTANCE = new LogTableColumn();
	
	public static final Column TIME = new Column("時刻") {
		
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getTime();
		}

		@Override
		public String getId() {
			return "TIME";
		}
	};
	
	public static final Column THREAD_NAME = new Column("スレッド名") {
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getThreadName();
		}
		
		@Override
		public String getId() {
			return "THREAD_NAME";
		}
	};
	
	public static final Column SESSION_ID = new Column("セッションID") {
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getSessionId();
		}
		
		@Override
		public String getId() {
			return "SESSION_ID";
		}
	};
	
	public static final Column METHOD = new Column("メソッド") {
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getMethod();
		}
		
		@Override
		public String getId() {
			return "METHOD";
		}
	};
	
	public static final Column REQUEST_URL = new Column("リクエストURL") {
		@Override
		public String getValue(LogEntry logEntry) {
			// TODO 一覧ではホスト名は出さない => 設定で選べるようにする 
			String url = logEntry.getRequestUrl();
			
			if (url == null) {
				return null;
			}
			
			try {
				return new URL(url).getFile();
			} catch (MalformedURLException e) {
				return url;
			}
		}
		
		@Override
		public String getId() {
			return "REQUEST_URL";
		}
	};
	
	public static final Column ELAPSED_TIME = new Column("経過時間") {
		@Override
		public String getValue(LogEntry logEntry) {
			// 一覧にはミリ秒で表示する
			
			Long elapsedTime = logEntry.getElapsedTime();
			
			if (elapsedTime == null) {
				return null;
			}
			
			return String.valueOf(elapsedTime / 1000000);
		}
		
		@Override
		public String getId() {
			return "ELAPSED_TIME";
		}
	};
	
	/** インデックスからColumnを取得するマップ */
	private Map<Integer, Column> indexMap = new HashMap<Integer, Column>();
	
	/** Columnからインデックスを取得するマップ */
	private Map<Column, Integer> reverseIndexMap = new HashMap<Column, Integer>();
	
	public static LogTableColumn getInstance() {
		return INSTANCE;
	}
	
	public int getColumnCount() {
		return getIndexMap().size();
	}

	public String getColumnName(Integer columnIndex) {
		
		Column column = getIndexMap().get(columnIndex);
		return column != null ? column.getName() : null;
	}
	
	public String getValue(LogEntry logEntry, int columnIndex) {
		
		Column column = getIndexMap().get(columnIndex);
		
		return column != null ? column.getValue(logEntry) : null;
	}
	
	private Map<Integer, Column> getIndexMap() {
		
		if (this.indexMap.isEmpty()) {
			setDefaultColumnMap();
		}
		
		return this.indexMap;
	}
	
	private Map<Column, Integer> getReverseIndexMap() {
		
		if (this.reverseIndexMap.isEmpty()) {
			setDefaultColumnMap();
		}
		
		return this.reverseIndexMap;
	}
	
	private void setDefaultColumnMap() {
		
		this.indexMap.put(0, TIME);
		this.indexMap.put(1, THREAD_NAME);
		this.indexMap.put(2, SESSION_ID);
		this.indexMap.put(3, METHOD);
		this.indexMap.put(4, REQUEST_URL);
		this.indexMap.put(5, ELAPSED_TIME);
		
		for (Map.Entry<Integer, Column> e : this.indexMap.entrySet()) {
			this.reverseIndexMap.put(e.getValue(), e.getKey());
		}
	}
	
	public static abstract class Column {
		
		private String name;
		
		public Column(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public abstract String getValue(LogEntry logEntry);
		
		public abstract String getId();
		
		@Override
		public int hashCode() {
			return getId().hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (obj == null || !(obj instanceof Column)) {
				return false;
			}
			
			return getId().equals( ((Column) obj).getId() );
		}
		
		@Override
		public String toString() {
			return getId() + "/" + getName();
		}
	}
	
}
