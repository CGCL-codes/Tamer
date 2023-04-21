package com.akivasoftware.misc.gui.widgets;

// Import statements
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelListener;

import com.akivasoftware.misc.gui.ModelFrame;

import java.util.Properties;
import java.util.Enumeration;
import java.util.Vector;

/**
 *  ATableModel is an Akiva specialized <code>TableModel</code> class
 *  @author J.Varner 
 */
public class APropertiesTableModel extends AbstractTableModel {
    // Class/instance attributes
    final String[] colName={"property","value"};
    final Object[][] data=new Object[10][10];
    private Vector vName;
    private Vector vValue;
    
    
    public APropertiesTableModel(){
        // Initialize vector
        vName=new Vector();
        vValue=new Vector();
    }

    public Object getValueAt(int row, int col) {
        return(data[row][col]);
    }
    
    public void setValueAt(Object obj,int row,int col){
        // Replace value at (row,col)
        data[row][col]=obj;
        
        // How do I update the underlying domain object?
        // Soln: Reach out to model tree and grab selected node. Then
        // set this node as a treeChangeListener
        ModelFrame frame=ModelFrame.getInstance();
        this.addTableModelListener(frame.getSelectedNode());
        
        // Fire table update event
        this.fireTableCellUpdated(row,col);
        
        // Remove selected node from listner list
        this.removeTableModelListener(frame.getSelectedNode());
    }
    
    public int getRowCount() {
        return(vName.size());
    }    
    
    public int getColumnCount() {
        return(colName.length);
    }
    
    public String getColumnName(int col){
        return(colName[col]);
    }
    
    public Class getColumnClass(int c){
        return getValueAt(0,c).getClass();
    }
    
    public boolean isCellEditable(int row,int col){
        boolean flag=false;
        
        if (col > 0){
            flag=true;
        }
        return(flag);
    }
    
    public void setProperties(Properties prop) {
        
        try {
            // Clear vectors
            vName.clear();
            vValue.clear();
            
            // Go through properties
            Enumeration enum=prop.keys();
            while (enum.hasMoreElements()){
                Object obj=enum.nextElement();
                
                // Debug
                System.out.println("setProperties["+obj+"]");
                
                vName.addElement(obj);
                vValue.addElement(prop.get(obj));
            }
            
            for (int q=0;q<vName.size();q++){
                data[q][0]=vName.elementAt(q);
            }
            
            for (int w=0;w<vValue.size();w++){
                data[w][1]=vValue.elementAt(w);
            }
            
            // Updates table??
            this.fireTableDataChanged();
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }
}
