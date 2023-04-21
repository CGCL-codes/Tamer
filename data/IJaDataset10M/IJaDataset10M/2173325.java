package ezsudoku.controller.jess;

import jess.JessException;
import jess.Userfunction;
import jess.ValueVector;
import jess.Context;
import jess.Value;
import ezsudoku.model.PlateCoords;
import ezsudoku.model.PlateModel;
import ezsudoku.controller.PlateControler;
import ezsudoku.view.ItemView;

/**
 * @author Cedric Chantepie (cchantepie@corsaire.fr)
 */
public class MostCandidateFunction implements Userfunction {

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "most-candidate";
    }

    /**
     * {@inheritDoc}
     */
    public Value call(ValueVector args, Context ctx) throws JessException {
        PlateControler controller = (PlateControler) args.get(1).externalAddressValue(ctx);
        int col = args.get(2).intValue(ctx);
        int line = args.get(3).intValue(ctx);
        int candidate = args.get(4).intValue(ctx);
        boolean most = (args.get(5).intValue(ctx) == 1);
        PlateCoords coords;
        ItemView itemView = null;
        if (col != -1 && line != -1) {
            coords = new PlateCoords(col, line);
            itemView = controller.getItemView(coords);
            if (itemView == null) {
                System.err.println("No item view");
                return new Value(false);
            }
            itemView.setMostCandidate(new Integer(candidate), most);
        } else if (col == -1 && line != -1) {
            for (int c = 0; c < PlateModel.COLUMN_NUMBER; c++) {
                coords = new PlateCoords(c, line);
                itemView = controller.getItemView(coords);
                if (itemView == null) {
                    continue;
                }
                itemView.setMostCandidate(new Integer(candidate), most);
            }
        } else if (col != -1 && line == -1) {
            for (int l = 0; l < PlateModel.LINE_NUMBER; l++) {
                coords = new PlateCoords(col, l);
                itemView = controller.getItemView(coords);
                if (itemView == null) {
                    continue;
                }
                itemView.setMostCandidate(new Integer(candidate), most);
            }
        } else {
            throw new IllegalArgumentException();
        }
        return new Value(true);
    }
}
