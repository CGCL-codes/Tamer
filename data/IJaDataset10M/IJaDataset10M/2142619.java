package ru.nosport.matrixaria;

/**
 * User: vfabr
 * Date: 27.10.2006
 * Time: 12:56:29
 */
public class WinMatrixariaService {

    public static void main(String[] _args) {
        if (_args.length < 1) {
            System.out.println("Error!");
            System.exit(1);
        }
        System.out.println("----------------");
        if (_args.length == 2 && _args[0].toLowerCase().equals("start")) {
            MatrixariaMain.initialize(_args[1]);
            MatrixariaMain mm = MatrixariaMain.getInstance();
            System.out.println("START!");
            mm.startServer();
        } else {
            MatrixariaMain mm = MatrixariaMain.getInstance();
            System.out.println("STOP!");
            mm.shutdownServer();
        }
    }
}
