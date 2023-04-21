package bitWave.physics;

public class Material {

    public static final double MW_Hydrogen = 2.0;

    public static final double MW_Helium = 4.0;

    public static final double MW_Methane = 16.0;

    public static final double MW_Ammonia = 17.0;

    public static final double MW_Water = 18.0;

    public static final double MW_Neon = 20.02;

    public static final double MW_Nitrogen = 28.0;

    public static final double MW_Air = 28.9;

    public static final double MW_Oxygen = 32.0;

    public static final double MW_Argon = 29.9;

    public static final double MW_Carbondioxyde = 44.0;

    private String strName;

    private double realDensity;

    /**
     * Creates a new material with the given name and the given density in kilograms per cubic meter.
     * @param strMaterialName
     * @param realMaterialDensity
     */
    public Material(final String strMaterialName, final double realMaterialDensity) {
        this.strName = strMaterialName;
        this.realDensity = realMaterialDensity;
    }

    public String getName() {
        return this.strName;
    }

    public double getDensity() {
        return this.realDensity;
    }

    public static final Material BRF3 = new Material("BROMINE TRIFLUORIDE", 2803);

    public static final Material BRF5_L = new Material("BROMINE PENTAFLUORIDE LIQUID", 2460);

    public static final Material CFN3O6 = new Material("MFTNMF / MONOFLUOROTRINITROMETHIDE", 1587);

    public static final Material CF4O2_L = new Material("BIS(FLUOROXY)DIFLUOROMETHANE", 1200);

    public static final Material CF7N3 = new Material("COMPOUND R / TRIS(DIFLUOROAMINO)FLUOROMETHANE", 156.);

    public static final Material CF8N4 = new Material("DELTA / TETRAKIS(DIFLUOROAMINO)METHANE", 1748);

    public static final Material CHN3O6_L = new Material("TNM / TRINITROMETHANE / NITROFORM", 1597);

    public static final Material CN4O8 = new Material("TNM / TETRANITROMETHANE", 1640);

    public static final Material C2H2N4O9 = new Material("TRINITROETHYL NITRATE", 1650);

    public static final Material C2H5NO2 = new Material("NITROETHANE", 1041);

    public static final Material C2H5NO3 = new Material("ETHYL NITRATE", 1100);

    public static final Material NH4_N3O4 = new Material("ADN / AMMONIUM DINITRAMIDE / NH4 N(NO2)2 / by SRI,1991, mp 95C, stable", 1800);

    public static final Material C3H5N3O9 = new Material("NG / NITROGLYCERINE / GLYCEROL TRINITRATE / O2NO-CH-[OCH2(NO2)]2", 1600);

    public static final Material C3H6N2O4_A = new Material("1,1-DINITROPROPANE", 1261);

    public static final Material C3H6N2O4_B = new Material("1,3-DINITROPROPANE", 1354);

    public static final Material CLF3 = new Material("CTF / CHLORINE TRIFLUORIDE", 1807);

    public static final Material CLF3O = new Material("FLOROX / OXYCHLORINE TRIFLUORIDE", 1852);

    public static final Material CLF5 = new Material("COMPOUND A / CHLORINE PENTAFLUORIDE", 1779);

    public static final Material CL2 = new Material("CHLORINE", 1560);

    public static final Material F2 = new Material("FLUORINE LIQUID AT NBP", 1505);

    public static final Material OF2 = new Material("OXYGEN DIFLUORIDE LIQUID AT NBP", 1521);

    public static final Material NF3 = new Material("NITROGEN TRIFLUORIDE LIQUID AT NBP", 1531);

    public static final Material HNO3 = new Material("NITRIC ACID", 1503);

    public static final Material HTP = new Material("HTP;PEROXIDE;HYDROGEN PEROXIDE", 1442);

    public static final Material N2O4 = new Material("NTO / NITROGEN TETROXIDE / DINITROGEN TETROXIDE", 1431);

    public static final Material O2 = new Material("LOX / LO2 / LIQUID OXYGEN AT NBP", 1149);

    public static final Material O3 = new Material("OZONE LIQUID AT NBP", 1449);

    public static final Material ALB3H12 = new Material("ABH / ALUMINUM BOROHYDRIDE", 550);

    public static final Material B2H6 = new Material("DB / DIBORANE LIQUID AT 180.59K (NBP)", 437);

    public static final Material B5H9 = new Material("PB / PENTABORANE", 640);

    public static final Material CH4 = new Material("METHANE LIQUID AT NBP", 424);

    public static final Material CH3N2H3 = new Material("MMH / MONOMETHYL HYDRAZINE / METHYL HYDRAZINE", 874);

    public static final Material C2H2 = new Material("ACETYLENE / ETHYNE LIQUID", 610);

    public static final Material C2H4 = new Material("ETHYLENE LIQUID AT NBP", 569);

    public static final Material C2H5N3O = new Material("2-TRIAZOETHANOL", 1149);

    public static final Material C2H6N2O = new Material("N,N-DIMETHYL NITROSOAMINE", 1005);

    public static final Material C2H8N2 = new Material("UDMH / UNSYM-DIMETHYLHYDRAZINE", 786);

    public static final Material ALB3H19C2N = new Material("HYBALINE A-4 / ALUMINUM BOROHYDRIDE DIMETHYLAMINATE", 734);

    public static final Material HN3 = new Material("HYDROGEN AZIDE / HYDRAZOIC ACID", 1091);

    public static final Material H2 = new Material("LH2 / HYDROGEN / HYDROGEN LIQUID AT NBP", 071);

    public static final Material NH3 = new Material("NH3 L / AMMONIA L", 676);

    public static final Material N2H4 = new Material("HYDRAZINE", 1004);

    public static final Material C10H20 = new Material("DECH / DIETHYLCYCLOHEXANE", 804);

    public static final Material JP_5 = new Material("JP-5", 830);

    public static final Material SIH4 = new Material("SILANE / SILICON TETRAHYDRIDE AT NBP", 680);

    public static final Material C3H8 = new Material("PROPANE / LIQUID PROPANE", 585);

    public static final Material C2H6 = new Material("ETHANE L", 570);

    public static final Material C3H4 = new Material("METHYLACETYLENE LIQ", 700);

    public static final Material CH1_9532 = new Material("RP-1 / KEROSENE / ROCKET PROPELLANT 1", 800);

    public static final Material ALB3H17CN = new Material("HYBALINE A5 / A5 / ALUMINUM BOROHYDRIDE METHYLAMINATE / AL(BH4)3:CH3NH2", 736);

    public static final Material N2 = new Material("NITROGEN LIQUID AT NBP", 808);

    public static final Material CF2N4O8 = new Material("1,1,2,2-TETRANITRO-1,2-DIFLUORIDE", 1696);

    public static final Material C2H4F4N2 = new Material("1,2-BIS(DIFLUOROAMINO)ETHANE", 1696);

    public static final Material N2F4 = new Material("N2F4 L / TETRAFLUOROHYDRAZINE LIQUID", 1660);

    public static final Material NH4CLO4 = new Material("AP / AMMONIUM PERCHLORATE", 1950);

    public static final Material NH4NO3 = new Material("AN / AMMONIUM NITRATE", 1725);

    public static final Material CH4N4O2 = new Material("NQ / NGu / NGU / NITROGUANIDINE / PICRITE", 1775);

    public static final Material CH5N5O6 = new Material("HNF / HYDRAZINIUM NITROFORMATE", 1850);

    public static final Material CH9N7O3 = new Material("TAGN / TRIAMINOGUANIDINIUM NITRATE", 1538);

    public static final Material C2H3N3O6 = new Material("1,1,1-TRINITROETHANE", 1530);

    public static final Material C2H4N6O8 = new Material("TNEDA / N,N,N',N'-TETRANITROETHYLENEDIAMINE", 1750);

    public static final Material C2H6N4O4 = new Material("EDNA / ETHYLENE DINITRAMINE", 1750);

    public static final Material C2N6O12 = new Material("HNE / HEXANITROETHANE", 2250);

    public static final Material C3H2F12N8O = new Material("BTU / N,N'-DI(TRIS(DIFLUOROAMINO)METHYL)UREA", 1880);

    public static final Material C3H6N2O4 = new Material("2,2-DINITROPROPANE", 1300);

    public static final Material C3H6N6O6 = new Material("RDX / HEXAHYDRO-1,3,5-TRINITRO-S-TRIAZINE", 1816);

    public static final Material NOCLF4 = new Material("NITROSYL TETRAFLUOROCHLORATE", 2850);

    public static final Material NH4OCLO4 = new Material("NH3OHCLO4 / HAP / HYDROXYLAMMONIUM PERCHLORATE", 2126);

    public static final Material N2H5CLO4 = new Material("HP / HYDRAZINIUM PERCHLORATE", 1940);

    public static final Material KCLO4 = new Material("POTASSIUM PERCHLORATE", 2520);

    public static final Material LICLO4 = new Material("LITHIUM PERCHLORATE", 2430);

    public static final Material NOCLO4 = new Material("NITROSYL PERCHLORATE", 2169);

    public static final Material NO2CLO4 = new Material("NP / NITRONIUM PERCHLORATE", 2200);

    public static final Material N2H6CL2O8 = new Material("HDP / HYDRAZINIUM DIPERCHLORATE", 2210);

    public static final Material LINO3 = new Material("LITHIUM NITRATE", 2380);

    public static final Material ALH3 = new Material("ALUMINUM HYDRIDE", 1430);

    public static final Material LIALH4 = new Material("LAH / LITHIUM ALUMINUM HYDRIDE", 917);

    public static final Material AL2H8MG = new Material("MAH / MAGNESIUM ALUMINUM HYDRIDE", 1046);

    public static final Material LIBH4 = new Material("LBH / LITHIUM BOROHYDRIDE", 681);

    public static final Material NABH4 = new Material("NABH / SODIUM BOROHYDRIDE", 1080);

    public static final Material NH3BH3 = new Material("AMMONIA BORANE / BORINE AMMONIATE", 730);

    public static final Material BEB2H8 = new Material("BERYLLIUM BOROHYDRIDE", 604);

    public static final Material B2H10N2 = new Material("B2H6.N2H4 / HYDRAZINE DIBORANE", 940);

    public static final Material B2H12N2 = new Material("DIAMMINO DIBORANE", 1000);

    public static final Material B10H18N2 = new Material("DEKAZENE / DODECAHYDRODECABORATE DIAMMINE", 1000);

    public static final Material CH2N4O = new Material("5-HYDROXYTETRAZOLE", 1760);

    public static final Material CH3N5 = new Material("5-AMINOTETRAZOLE", 1650);

    public static final Material CH8N6 = new Material("TAG / TRIAMINOGUANIDINE", 1560);

    public static final Material CH9N9 = new Material("TAZ / TRIAMINOGUANIDINIUM AZIDE", 1440);

    public static final Material ACETYLENE = new Material("C2H2 / ACETYLENE / ETHYNE SOLID", 729);

    public static final Material C2H4N4 = new Material("DICYANDIAMIDE", 1400);

    public static final Material C2H8N8 = new Material("DIAMINOGUANIDINIUM AZIDE-FORMALDEHYDE POLYMER / DAZAL", 1420);

    public static final Material LIH = new Material("LITHIUM HYDRIDE", 8200);

    public static final Material NH3N3 = new Material("AMMONIUM AZIDE", 1346);

    public static final Material KN3 = new Material("POTASSIUM AZIDE", 2040);

    public static final Material NAN3 = new Material("SODIUM AZIDE", 1850);

    public static final Material BE = new Material("BERYLLIUM", 1850);

    public static final Material BE26H59C4 = new Material("BERYLLIUM HYDRIDE 83%", 660);

    public static final Material BE8_7182H17_ = new Material("BERYLLIUM HYDRIDE 95%", 650);

    public static final Material B = new Material("BORON", 2320);

    public static final Material LI = new Material("LITHIUM", 534);

    public static final Material MG = new Material("MAGNESIUM", 1740);

    public static final Material MGH2 = new Material("MAGNESIUM HYDRIDE", 1450);

    public static final Material NA = new Material("SODIUM", 9700);

    public static final Material PB = new Material("LEAD", 11337);

    public static final Material S = new Material("SULFUR", 2046);

    public static final Material SI = new Material("SILICON", 2400);

    public static final Material TI = new Material("TITANIUM", 4500);

    public static final Material ZR = new Material("ZIRCONIUM", 6400);

    public static final Material C17H20N2O = new Material("EC / ETHYL CENTRALITE", 1140);

    public static final Material ALB9H24 = new Material("ALUMINUM TRIBOROHYDRIDE", 500);

    public static final Material ALB3H14C1O = new Material("ABHPMO / ALUMINUM BOROHYDRIDE POLYMETHYLENE OXIDE / AL(BH4)3:CH2O", 1800);

    public static final Material ALB3H20C4O2 = new Material("ABHBPMO / ALUMINUM BOROHYDRIDE BISPOLYETHYLENEOXIDE / AL(BH4)3:2C2H4O", 710);

    public static final Material FE2O3 = new Material("FERRIC OXIDE / HEMATITE", 5120);

    public static final Material C2F4 = new Material("TEFLON / POLYTETRAFLUOROETHYLENE", 2310);

    public static final Material PE = new Material("C2H4 / PE / POLYETHYLENE", 900);

    public static final Material CH2PE = new Material("PE / POLYETHYLENE", 900);

    public static final Material C2_238H3_24F = new Material("PBEP / POLY (1.2-BIS(DIFOUOROAMINO)-2,3-EPOXYPROPAN240 -113.000   .000 C3H5NO / POLYACRYLAMIDE", 1610);

    public static final Material CUBANE_A = new Material("C8H8 / CUBANE", 1180);

    public static final Material CUBANE_B = new Material("C8H8 / CUBANE / CUBANE WITH HT OF FORM BIASED TOWARD ARIES CLAIM", 1180);

    public static final Material CUBANE_C = new Material("C8H8 / CUBANE / CUBANE WITH ARIES HT OF FORMATION", 1180);

    public static final Material CSNO3 = new Material("CESIUM NITRATE", 3685);

    public static final Material C3H5N3O = new Material("GAP / GLYCIDYLAZIDE POLYMER / -OCH2CH(CH2N3)-", 1300);

    public static final Material C6H12N2O8 = new Material("TEGDN / TRIETHYLENEGLYCOL DINITRATE", 1329);

    public static final Material C7H8N2O2 = new Material("MNA / N-METHYL-P-NITROANILINE / CH3NH-Ph-NO2", 1210);

    public static final Material C = new Material("GRAPHITE", 1800);

    public static final Material C2H4N2O2 = new Material("OXAMIDE / OXAMIDE S / OXALAMIDE / ETHANEDIAMIDE / OXALIC ACID DIAMIDE", 1667);

    public static final Material N100 = new Material("N-100 / 23H38N6O5", 1132);

    public static final Material C8H12N2O2 = new Material("HDI / HMDI / HEXAMETHYLENE DIISOCYANATE / OCN-(CH2)6-NCO", 1040);

    public static final Material C15H21FEO6 = new Material("FE(AA) / FE(AA)3 / FERRIC ACETYLACETONATE / ((CH3CO)2CH)3FE", 1330);

    public static final Material C4H8N8O8 = new Material("HMX / CYCLOTETRAMETHYLENETETRANITRAMINE / HER MAJESTY'S EXPLOSIVE", 1930);

    public static final Material C5H9N3O9 = new Material("TMETN / METN / METRIOL TRINITRATE / 1,1,1-TRIMETHYLOLETHANE TRINITRATE", 1488);

    public static final Material C4H7N3O9 = new Material("BTTN / 1,2,4-BUTANETRIOL TRINITRATE", 1520);

    public static final Material C3H7CLF6N4O = new Material("INFO 631C / 2-TRIS(DIFLUORAMINO)METHOXY)ETHYLAMINE HYDROCHLORIDE", 1800);

    public static final Material C3H7CLF6N4O5 = new Material("INFO 635P / 2-TRIS(DIFLUORAMINO)METHOXY)ETHYLAMINE PERCHLORATE", 1800);

    public static final Material C5H6F2N4O10 = new Material("FEFO / [F(NO2)2CCH2O]2CH2", 1600);

    public static final Material C7F6H8N6O10 = new Material("SYEP / [FC(NO2)2CH2OCH2]2-C(NF2)2", 1650);

    public static final Material C11H14F10N8O = new Material("SYFO / [FC(NO2)2CH2CH2C(NF2)2CH2O]2-CH2", 1640);

    public static final Material C7_337H10_98 = new Material("R-45M / HTPB / HYDROXY TERMINATED POLYBUTADIENE", 900);

    public static final Material FEF3 = new Material("FERRIC FLUORIDE S", 3520);

    public static final Material PBO_LITHARGE = new Material("PBO / LEAD MONOXIDE / LITHARGE / RED PBO", 9530);

    public static final Material PBO_MASSICOT = new Material("PBO / LEAD MONOXIDE / MASSICOT / YELLOW PBO", 8000);

    public static final Material PBO2 = new Material("LEAD DIOXIDE / PLATTNERITE", 9375);

    public static final Material PB3O4 = new Material("LEAD ORTHOPLUMBATE / TRILEAD TETRAOXIDE / RED LEAD OXIDE", 9100);

    public static final Material C4H8N2O7 = new Material("DEGDN / DIETHYLENE GLYCOL DINITRATE / O2NOCH2CH2OCH2CH2ONO2", 1377);

    public static final Material C2H4N2O6 = new Material("EGDN / ETHYLENE GLYCOL DINITRATE / O2NOCH2CH2ONO2", 1480);

    public static final Material C3H6N2O6 = new Material("PGDN / PROPYLENEGLYCOL DINITRATE / O2NOCH2CH(ONO2)CH3", 1376);

    public static final Material CH6N4O3 = new Material("GN / GUANIDINE NITRATE", 1436);

    public static final Material C6H7_68N2_32 = new Material("NC / NITROCELLULOSE / 12.2%N (NC) / PYROXYLIN / CELLULOSE NITRAE", 1653);

    public static final Material CH7_365N2_64 = new Material("NC / NITROCELLULOSE / GRADE C NC / TYPE INC / 13.15%N (NC)", 1650);

    public static final Material C9H6N2O2 = new Material("TDI / TOLUENE DIISOCYANATE", 1220);

    public static final Material C9H14F12N6O3 = new Material("TVOPA / [[CH2(NF2)CH(NF2)O]CH2]2-CH-[OCH(NF2)CH2(NF2)]", 1540);

    public static final Material C12H18N2O2 = new Material("IPDI / ISOPHORONE DIISOCYANATE", 1060);

    public static final Material KNO3 = new Material("KN / POTASSIUM NITRATE / SALTPETER / NITER / NITRE / SALPRUNELLA", 2100);

    public static final Material C12H14O4 = new Material("DEP / DIETHYL PHTHALATE", 1120);

    public static final Material C16H22O4 = new Material("DBP / DIBUTYL PHTHALATE", 1045);

    public static final Material C10H10O4 = new Material("DMP / DIMETHYL PHTHALATE", 1190);

    public static final Material C6H6O2 = new Material("RESORCINOL / RESORCIN / 1,3-BENZENEDIOLE", 1200);

    public static final Material C12H10N2O2 = new Material("2-NDPA / 2-NITRODIPHENYLAMINE", 1366);

    public static final Material C12H11N = new Material("DPA / DIPHENYLAMINE", 1600);

    public static final Material C9H18N3OP = new Material("MAPO / TRISMETHYLAZIRIDINYLPHOSPHINEOXIDE / O=P[NCH2CHCH3]3", 1080);

    public static final Material C9H14O6 = new Material("TA / TRIACETIN / GLYCERINE TRIACETATE / [CH3COOCH2]2-CH-OCOCH3", 1160);

    public static final Material K2SO4 = new Material("POTASSIUM SULFATE", 2662);

    public static final Material C19H38O2 = new Material("IDP / ISODECYL PELARGONATE / CH3(CH2)7COO(CH2)7CH(CH3)2", 870);

    public static final Material C27H32FE2 = new Material("CATOCENE / 2,2-BIS(ETHYLFERROCENYL)PROPANE / [C2H5(C5H5)2FE]2-C(CH3)2", 1270);

    public static final Material C36H70O4PB = new Material("PBST / LEAD STEARATE / [CH3(CH2)16COO]2=PB", 1400);

    public static final Material ZRC = new Material("ZIRCONIUM CARBIDE", 6900);

    public static final Material CUSO4 = new Material("COPPER SULFATE / CUPRIC SULFATE", 3650);

    public static final Material C8H24B10 = new Material("NHC / N-HEXYL CARBORANE / CHB10H10C(CH2)5CH3", 900);

    public static final Material C23H32O2 = new Material("AO 2246 / antioxicant 2246", 1080);

    public static final Material C15H22O8 = new Material("CAB / CELLULOSE ACETATE/BUTYRATE", 1200);

    public static final Material C12H16O8 = new Material("CA / CELLULOSE ACETATE", 1300);

    public static final Material C10H10FE = new Material("FERROCENE / [C5H5]2=FE", 1490);

    public static final Material AL2O3 = new Material("ALUMINUM OXIDE / ALUMINA / CORUNDUM", 3970);

    public static final Material C14H24N18O4 = new Material("GAP-A / GLYCIDYL AZIDE-A POLYMER", 1240);

    public static final Material C7_1158H11_0 = new Material("HT BIND", 900);

    public static final Material C5_117_H9_58 = new Material("POLYU / POLYURETHANE", 1010);

    public static final Material C11F14H8 = new Material("VITON", 1850);

    public static final Material C2_53_H4_1_N = new Material("NPU / NITROPOLYURETHANE", 1500);

    public static final Material C64H94N34O18 = new Material("SBM / [-CH(C2H3N2F4)CH2O-]15-CONHC2H2N2F4NHCOO-", 1500);

    public static final Material C2_451_H3_27 = new Material("PBEP XL / CROSSLINKED PBEP", 1610);

    public static final Material C6_H4_N6_O12 = new Material("HNH / HEXANITROHEXYNE / [CCH2C(NO2)3]2 / mp 130, stable (Thiokol)", 1868);

    public static final Material C4_167_H11_7 = new Material("43 B / 43% BORON BINDER / 1 C10H22O4B10+ 1 C5H16B10", 967);

    public static final Material C2_618_H8_77 = new Material("55 B / 55% BORON BINDER / see 43B", 1027);

    public static final Material C2_15_H9_106 = new Material("65 B / 65% BORON BINDER / see 43 B", 1000);

    public static final Material C22_H42_O4 = new Material("DOA / DIOCTYL ADIPATE PLASTICIZER", 927);

    public static final Material C26_H50_O4 = new Material("DOS / DIOCTYL SEBACATE PLASTICIZER", 910);

    public static final Material NH3OHCN3O6 = new Material("HANF / HYDROXYLAMMONIUM NITROFORM / hypothetical material", 2000);

    public static final Material AL1_854MG2_0 = new Material("AL/MG / 50/50 AL/MG ALLOY", 2187);

    public static final Material MGAL2 = new Material("MAGNESIUM/ALUMINUM ALLOY / (density is estimated)", 2400);

    public static final Material H2O = new Material("Water", 1000);

    public static final Material ALCOHOL_ETHYL = new Material("Alcohol, ethyl at 25°C", 785.06);

    public static final Material ALCOHOL_METHYL = new Material("Alcohol, methyl at 25°C", 786.51);

    public static final Material ALCOHOL_PROPYL = new Material("Alcohol, propyl at 25°C", 799.96);

    public static final Material METHANE = new Material("Liquid Methane at -164°C", 785.06);

    public static final Material AL = new Material("Aluminium", 2640);

    public static final Material STEEL = new Material("Steel", 7870);

    public static final Material LOX = new Material("LOX", 1141);

    public static final Material LH2 = new Material("LH2", 70.8);

    public static final Material RP1 = new Material("RP-1", 810);
}
