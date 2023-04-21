package activationRegister.util;

import activationRegister.temp.Label;

/**
 * 
 * */
public abstract class Exp {

    /**
	 * 
	 * */
    public abstract treeIR.Exp unEx();

    /**
	 * 
	 * */
    public abstract treeIR.Stm unNx();

    /**
	 * 
	 * */
    public abstract treeIR.Stm unCx(Label t, Label f);
}
