package net.milkdrops;

public class CleanResCmd {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        CleanRes remover = new CleanRes(args[0]);
        remover.removeResources(null);
    }

    public void initUI() {
    }
}
