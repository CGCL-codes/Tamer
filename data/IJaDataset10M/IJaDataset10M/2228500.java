package net.kyllercg.acms;

/**
 * @author Kyller Costa Gorgônio
 * @version 1.1
 */
public class RRBBReaderModule extends PetriNet {

    private static int PEPcol100 = 100;

    private static int PEPcol220 = 220;

    private static int PEPcol500 = 500;

    private static int PEPcol560 = 560;

    private static int PEPcol620 = 620;

    private static int PEPln60 = 60;

    private static int PEPln140 = 140;

    private static int PEPln220 = 220;

    private static int PEPln300 = 300;

    private static int PEPln380 = 380;

    private static int PEPheight = PEPln380 - PEPln60;

    /**
	 * The number of the cell controled by the module
	 */
    private int cell;

    /**
	 * Class contructor
	 * 
	 * @param cell - the cell number that the module controls
	 */
    public RRBBReaderModule(int cell, int size) {
        this.cell = cell;
        int next = (this.cell + 1) % size;
        String rI = "ri" + this.cell;
        String rJ = "ri" + next;
        String prI = "pri" + this.cell;
        String reI = "re" + this.cell;
        String rneI = "rne" + this.cell;
        String reJ = "re" + next;
        String rneJ = "rne" + next;
        String weJ = "we" + next;
        String wneJ = "wne" + next;
        String rdI = "rd" + this.cell;
        String mII = "m" + this.cell + "_" + this.cell;
        String mIJ = "m" + this.cell + "_" + next;
        addPlace(PetriNet.createPlace(rI, ((cell == 0) ? 1 : 0), PEPcol560, (PEPln60 + (PEPheight * this.cell))));
        addPlace(PetriNet.createPlace(rJ, 0, PEPcol560, (PEPln60 + (PEPheight * next))));
        addPlace(PetriNet.createPlace(prI, 0, PEPcol560, (PEPln220 + (PEPheight * this.cell))));
        addPlace(PetriNet.createPlace(reI, ((cell == 0) ? 1 : 0), PEPcol500, (PEPln220 + (PEPheight * this.cell))));
        addPlace(PetriNet.createPlace(rneI, ((cell == 0) ? 0 : 1), PEPcol620, (PEPln220 + (PEPheight * this.cell))));
        addPlace(PetriNet.createPlace(reJ, ((next == 0) ? 1 : 0), PEPcol500, (PEPln220 + (PEPheight * next))));
        addPlace(PetriNet.createPlace(rneJ, ((next == 0) ? 0 : 1), PEPcol620, (PEPln220 + (PEPheight * next))));
        addPlace(PetriNet.createPlace(weJ, ((next == 1) ? 1 : 0), PEPcol100, (PEPln220 + (PEPheight * next))));
        addPlace(PetriNet.createPlace(wneJ, ((next == 1) ? 0 : 1), PEPcol220, (PEPln220 + (PEPheight * next))));
        addTransition(PetriNet.createTransition(rdI, PEPcol560, (PEPln140 + (PEPheight * this.cell)), this.cell));
        addTransition(PetriNet.createTransition(mII, PEPcol620, (PEPln140 + (PEPheight * this.cell)), this.cell, this.cell));
        addTransition(PetriNet.createTransition(mIJ, PEPcol560, (PEPln300 + (PEPheight * this.cell)), this.cell, next));
        addArc(PetriNet.createArc(existsPlace(rI), existsTransition(rdI), Arc.place2trans));
        addArc(PetriNet.createArc(existsPlace(reI), existsTransition(mIJ), Arc.place2trans));
        addArc(PetriNet.createArc(existsPlace(rneJ), existsTransition(mIJ), Arc.place2trans));
        addArc(PetriNet.createArc(existsPlace(prI), existsTransition(mII), Arc.place2trans));
        addArc(PetriNet.createArc(existsPlace(prI), existsTransition(mIJ), Arc.place2trans));
        addArc(PetriNet.createArc(existsTransition(rdI), existsPlace(prI), Arc.trans2place));
        addArc(PetriNet.createArc(existsTransition(mII), existsPlace(rI), Arc.trans2place));
        addArc(PetriNet.createArc(existsTransition(mIJ), existsPlace(rJ), Arc.trans2place));
        addArc(PetriNet.createArc(existsTransition(mIJ), existsPlace(rneI), Arc.trans2place));
        addArc(PetriNet.createArc(existsTransition(mIJ), existsPlace(reJ), Arc.trans2place));
        addArc(PetriNet.createArc(existsTransition(mII), existsPlace(weJ), Arc.test));
        addArc(PetriNet.createArc(existsTransition(mIJ), existsPlace(wneJ), Arc.test));
    }
}
