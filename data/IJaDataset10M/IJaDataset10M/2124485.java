package harvestmars;

/**
 *
 * @author toshiba
 */
public class MatriksTanaman {

    public Tanaman[][] MatTan = new Tanaman[11][19];

    public MatriksTanaman() {
        for (int y = 0; y <= 18; y++) {
            for (int x = 0; x <= 10; x++) {
                MatTan[x][y] = new Tanaman();
            }
        }
    }
}
