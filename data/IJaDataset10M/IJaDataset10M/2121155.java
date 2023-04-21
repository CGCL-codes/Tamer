package br.unb.entities.signalControllers.Fuzzy;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import br.unb.entities.signalControllers.SignalPhase;
import br.unb.main.ModelController;

public class FuzzySignalContrMultTablesExtEnd extends FuzzySignalController {

    List<ExtensionTableWIthQueueLevel> tablesList;

    public FuzzySignalContrMultTablesExtEnd(ModelController modelController) {
        super(modelController);
        name = "Fuzzy FE";
        tablesList = new ArrayList<ExtensionTableWIthQueueLevel>();
    }

    @Override
    public void addPhase(SignalPhase signalPhase) {
        super.addPhase(signalPhase);
        ((FuzzySignalPhaseMultTablesExtEnd) signalPhase).setListOfTables(tablesList);
    }

    public void setTableListOnAllPhases() {
        if (phaseList != null) {
            for (SignalPhase p : phaseList) {
                ((FuzzySignalPhaseMultTablesExtEnd) p).setListOfTables(tablesList);
            }
        }
    }

    public List<ExtensionTableWIthQueueLevel> getTablesList() {
        return tablesList;
    }

    public void paint(Graphics2D g2) {
        if (rectangle != null) {
            if (selected) {
                g2.setColor(colors[1]);
                g2.draw(rectangle);
            } else {
                g2.setColor(colors[0]);
            }
            g2.setStroke(new BasicStroke((float) 1));
            g2.draw(rectangle);
            Font font = new Font("Dialog", Font.PLAIN, 6);
            g2.setFont(font);
            g2.drawString(name, (int) (x + 2), (int) (y + 6));
            NumberFormat f = DecimalFormat.getInstance(UIManager.getDefaults().getDefaultLocale());
            f.setMaximumFractionDigits(3);
            f.setMinimumFractionDigits(1);
            g2.drawString("Tempo de verde:", (int) (x + 2), (int) (y + 12));
            g2.drawString(f.format(this.getTimeOfGreen()), (int) (x + 54), (int) (y + 12));
            if (phase != null) {
                FuzzySignalPhaseMultTablesExtEnd fuzPhase = (FuzzySignalPhaseMultTablesExtEnd) phase;
                g2.drawString("Nro Extensão:", (int) (x + 2), (int) (y + 18));
                g2.drawString(Integer.toString(fuzPhase.getNumOfExtensionsGiven()), (int) (x + 46), (int) (y + 18));
                g2.drawString("Extensão da tabela:", (int) (x + 2), (int) (y + 24));
                g2.drawString(f.format(fuzPhase.getTargetExtension()), (int) (x + 63), (int) (y + 24));
                g2.drawString("Tempo de extensão:", (int) (x + 2), (int) (y + 30));
                g2.drawString(f.format(fuzPhase.getActualExtension()), (int) (x + 63), (int) (y + 30));
                g2.drawString("Tabela:", (int) (x + 2), (int) (y + 36));
                g2.drawString(Integer.toString(tablesList.get(fuzPhase.getTableIndex()).queue), (int) (x + 30), (int) (y + 36));
            }
        }
    }
}
