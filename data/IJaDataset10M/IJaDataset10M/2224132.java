package net.mikaboshi.servlet.monitor.viewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.mikaboshi.servlet.monitor.LogEntry;

public final class LogTableColumn {
	
	private LogTableColumn() {}
	
	private static final LogTableColumn INSTANCE = new LogTableColumn();
	
	public static final Column TIME = new Column("����") {
		
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getTime();
		}

		@Override
		public String getId() {
			return "TIME";
		}
	};
	
	public static final Column THREAD_NAME = new Column("�X���b�h��") {
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getThreadName();
		}
		
		@Override
		public String getId() {
			return "THREAD_NAME";
		}
	};
	
	public static final Column SESSION_ID = new Column("�Z�b�V����ID") {
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getSessionId();
		}
		
		@Override
		public String getId() {
			return "SESSION_ID";
		}
	};
	
	public static final Column METHOD = new Column("���\�b�h") {
		@Override
		public String getValue(LogEntry logEntry) {
			return logEntry.getMethod();
		}
		
		@Override
		public String getId() {
			return "METHOD";
		}
	};
	
	public static final Column REQUEST_URL = new Column("���N�G�X�gURL") {
		@Override
		public String getValue(LogEntry logEntry) {
			// TODO �ꗗ�ł̓z�X�g���͏o���Ȃ� => �ݒ�őI�ׂ�悤�ɂ��� 
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
	
	public static final Column ELAPSED_TIME = new Column("�o�ߎ���") {
		@Override
		public String getValue(LogEntry logEntry) {
			// �ꗗ�ɂ̓~���b�ŕ\������
			
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
	
	/** �C���f�b�N�X����Column���擾����}�b�v */
	private Map<Integer, Column> indexMap = new HashMap<Integer, Column>();
	
	/** Column����C���f�b�N�X���擾����}�b�v */
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
