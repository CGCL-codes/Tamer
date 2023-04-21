package com.flca.pstc42;

import com.flca.pstc42.dto.AdresDto;
import com.flca.pstc42.dto.DistanceDto;
import com.flca.pstc42.util.PstcUtils;

public class Psct42ToolsMain {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Psct42ToolsMain pcmain = new Psct42ToolsMain();
        if (args.length >= 2) {
            if (args[0].equals("find")) {
                if (args.length == 3) {
                    pcmain.find(args[1], args[2]);
                } else if (args.length == 2) {
                    pcmain.find(args[1]);
                } else {
                    usage();
                }
            } else if (args[0].toLowerCase().equals("distance") && args.length == 5) {
                pcmain.distance(args[1], args[2], args[3], args[4]);
            } else if (args[0].toLowerCase().equals("distance") && args.length == 3) {
                pcmain.distance(args[1], args[2]);
            } else {
                usage();
            }
        } else {
            usage();
        }
    }

    private static void usage() {
        System.out.println("find <postcode> <[housnr]> ");
        System.out.println("examples:");
        System.out.println("   find 5672VB 67");
        System.out.println("   find 5672VB");
        System.out.println("   find 5672");
        System.out.println("");
        System.out.println("distance <pc1> <[nr1]> <pc2> <[nr2]>");
        System.out.println("examples:");
        System.out.println("   distance 5672VB 67 6041SL 139");
        System.out.println("   distance 5672VB 6041SL");
        System.out.println("   distance 5672 6041");
    }

    public void find(String postcode, String huisnr) {
        if (postcode.length() == 4) {
            int pcnum = Integer.parseInt(postcode.substring(0, 4));
            AdresDto adres;
            try {
                adres = PstcUtils.findAdres(pcnum);
                System.out.println(adres);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            int pcnum = Integer.parseInt(postcode.substring(0, 4));
            String pcalfa = postcode.substring(4);
            int nr = Integer.parseInt(huisnr);
            AdresDto adres;
            try {
                adres = PstcUtils.findAdres(pcnum, pcalfa, nr);
                System.out.println(adres);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void find(String postcode) {
        int pcnum = Integer.parseInt(postcode.substring(0, 4));
        if (postcode.length() == 6) {
            String pcalfa = postcode.substring(4);
            AdresDto adres;
            try {
                adres = PstcUtils.findAdres(pcnum, pcalfa);
                System.out.println(adres);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AdresDto adres;
            try {
                adres = PstcUtils.findAdres(pcnum);
                System.out.println(adres);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void distance(String aPc1, String aNr1, String aPc2, String aNr2) {
        int pcnum1 = Integer.parseInt(aPc1.substring(0, 4));
        String pcalfa1 = aPc1.substring(4);
        int nr1 = Integer.parseInt(aNr1);
        int pcnum2 = Integer.parseInt(aPc2.substring(0, 4));
        String pcalfa2 = aPc2.substring(4);
        int nr2 = Integer.parseInt(aNr2);
        AdresDto adres1;
        try {
            adres1 = PstcUtils.findAdres(pcnum1, pcalfa1, nr1);
            AdresDto adres2 = PstcUtils.findAdres(pcnum2, pcalfa2, nr2);
            System.out.println(adres1);
            System.out.println(adres2);
            DistanceDto distdto = PstcUtils.calcDistance(adres1, adres2);
            System.out.println("" + distdto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void distance(String aPc1, String aPc2) {
        try {
            AdresDto adres1;
            AdresDto adres2;
            int pcnum1 = Integer.parseInt(aPc1.substring(0, 4));
            if (aPc1.length() == 6) {
                String pcalfa1 = aPc1.substring(4);
                adres1 = PstcUtils.findAdres(pcnum1, pcalfa1);
            } else {
                adres1 = PstcUtils.findAdres(pcnum1);
            }
            int pcnum2 = Integer.parseInt(aPc2.substring(0, 4));
            if (aPc2.length() == 6) {
                String pcalfa2 = aPc2.substring(4);
                adres2 = PstcUtils.findAdres(pcnum2, pcalfa2);
            } else {
                adres2 = PstcUtils.findAdres(pcnum2);
            }
            System.out.println(adres1);
            System.out.println(adres2);
            DistanceDto distdto = PstcUtils.calcDistance(adres1, adres2);
            System.out.println("" + distdto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
