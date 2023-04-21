/*
 *  (c) Frank Jakop, 2006
 *  Created on 18.12.2006
 *
 *  $Author: maximalz $
 *  $Revision: 1.4 $
 *  $Source: /cvsroot/uwrugbyhupe/uwrugbyhupe/src/java/de/jakop/rugby/team/TeamTableModel.java,v $
 *  $Id: TeamTableModel.java,v 1.4 2007/09/12 06:58:48 maximalz Exp $
 *  $Date: 2007/09/12 06:58:48 $
 *
 *  $Log: TeamTableModel.java,v $
 *  Revision 1.4  2007/09/12 06:58:48  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.3  2007/03/19 14:22:42  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.2  2007/02/10 18:18:07  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.1.2.2  2007/02/10 18:09:53  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.1.2.1  2007/02/10 15:17:12  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.2.2.1  2007/02/09 13:48:06  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.2  2007/01/27 11:53:14  maximalz
 *  Teams als Repository für Strafzeiten
 *
 *  Revision 1.1  2007/01/27 10:42:48  maximalz
 *  Team als Klasse refactored
 *
 *  Revision 1.1  2007/01/27 07:46:22  maximalz
 *  *** empty log message ***
 *
 *  Revision 1.5  2007/01/16 07:48:22  maximalz
 *  Anwendungskontrolle begonnen
 *
 *  Revision 1.4  2007/01/14 14:32:00  maximalz
 *  i18n fertig
 *
 *  Revision 1.3  2007/01/14 13:45:08  maximalz
 *  i18n fertig bis auf Zustände
 *
 *  Revision 1.2  2007/01/09 13:08:32  maximalz
 *  zustände -> zustand
 *  cobertura Coverage Tool
 *  build.xml
 *
 *  Revision 1.1  2007/01/04 17:13:44  maximalz
 *  initial revision (CVS switch)
 *
 *  Revision 1.6  2006/12/20 13:59:14  jakop
 *  Kontroller implementiert, Zustandswechsel der Kontrollen über Kontroller, nicht über Views
 *
 *  Revision 1.5  2006/12/19 14:06:33  jakop
 *  Anfang ResourceMgr
 *
 *  Revision 1.4  2006/12/19 12:20:50  jakop
 *  JavaDoc
 *
 *  Revision 1.3  2006/12/19 12:14:00  jakop
 *  Strafzeiten als Zustände in Liste, Hupe kein Observer mehr
 *
 *  Revision 1.2  2006/12/19 10:16:04  jakop
 *  Protokoll durch Zustände ersetzt
 *
 *  Revision 1.1  2006/12/18 08:13:52  jakop
 *  Uhrwechsel für Kontroller
 *
 *
 */
package de.jakop.rugby.team;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jakop.rugby.types.ISpieler;
import de.jakop.rugby.types.ITeam;
import de.jakop.rugby.util.ResourceManager;

/**
 * TableModel für die Anzeige von Mannschaftsaufstellungen.
 * Spalten:<br>
 * Nr., Name, Mannschaftsführer
 * 
 * @author jakop
 */
public class TeamTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3328548587840655579L;

	/** Field log Logger for this class */
	private final Log log = LogFactory.getLog(this.getClass());

	private String[] spaltenNamen;
	private ITeam aufstellung;

	/** */
	public TeamTableModel() {
		ResourceManager rm = ResourceManager.getManager(this.getClass());

		String spalteNr = rm.getString("Column_PlayerNr_Header");
		String spalteName = rm.getString("Column_PlayerName_Header");
		String spalteMf = rm.getString("Column_TeamLeader_Header");

		this.spaltenNamen = new String[] { spalteNr, spalteName, spalteMf };
	}

	public int getRowCount() {
		if (this.aufstellung == null) {
			return 0;
		}
		return this.aufstellung.getSpieler().size();
	}

	public int getColumnCount() {
		return 3;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex != 2;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(aValue + "," + rowIndex + "," + columnIndex);
		}
		ISpieler s = this.aufstellung.getSpieler().get(rowIndex);
		// TODO Bei Nr.-Änderung auf Doppelte prüfen
		// TODO Mannschaftsführer setzen
		if (columnIndex == 0) {
			s.setNr((Integer) aValue);
		} else if (columnIndex == 1) {
			s.setName((String) aValue);
		}
	}

	@Override
	public String getColumnName(int column) {
		return this.spaltenNamen[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Integer.class;
		} else if (columnIndex == 1) {
			return String.class;
		} else if (columnIndex == 2) {
			return Boolean.class;
		}
		return null;
	}

	public Object getValueAt(int row, int column) {
		if (this.aufstellung != null && row < this.aufstellung.getSpieler().size() && row > -1) {
			ISpieler s = this.aufstellung.getSpieler().get(row);

			if (column == 0) {
				return s.getNr();
			} else if (column == 1) {
				return s.getName();
			} else if (column == 2) {
				return s == this.aufstellung.getMannschaftsführer();
			}
		}
		return null;
	}

	/**
	 * Setzt eine neue Liste mit Spielern 
	 * @param team 
	 */
	public void setTeam(ITeam team) {
		this.aufstellung = team;
		fireTableDataChanged();
	}
}
