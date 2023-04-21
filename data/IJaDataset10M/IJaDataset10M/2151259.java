package compton.physics;

/**
 * This class contains physical constants
 *
 * Initially written in April 2008
 *
 * Some data (*) come from 
 * http://www.ee.ucl.ac.uk/~mflanaga/java/Fmath.html#const
 *
 * PDG indicates values taken from the PDG.
 *
 */
public class PhysicsConstants {

    public static final double PI = Math.PI;

    public static final double JOULES_TO_MEV = 6.24150974 * 1e12;

    public static final double N_AVAGADRO = 6.0221419947e23;

    public static final double K_BOLTZMANN = 1.380650324e-23;

    public static final double H_PLANCK = 6.6260687652e-34;

    public static final double H_C_MeV_fm = 1239.841185;

    public static final double H_RED_C_MeV_fm = 197.326968;

    public static final double H_PLANCK_RED = H_PLANCK / (2 * PI);

    public static final double C_LIGHT = 2.99792458e8;

    public static final double C_LIGHT_UM_NS = 2.99792458e5;

    public static final double R_GAS = 8.31447215;

    public static final double F_FARADAY = 9.6485341539e4;

    public static final double T_ABS = -273.15;

    public static final double Q_ELECTRON = -1.60217646263e-19;

    public static final double M_ELECTRON = 9.1093818872e-31;

    public static final double M_ELECTRON_MeV = 0.510998918;

    public static final double M_PROTON = 1.6726215813e-27;

    public static final double M_NEUTRON = 1.6749271613e-27;

    public static final double EPSILON_0 = 8.854187817e-12;

    public static final double MU_0 = Math.PI * 4.0e-7;

    public static final double EULER_CONSTANT_GAMMA = 0.5772156649015627;

    public static final double S_THOMPSON = 6.65246e-17;

    public static final double R_ELECTRON = 2.81794e-15;
}
