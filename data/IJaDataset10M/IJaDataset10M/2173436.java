package br.com.danielnegri.nfe.negocio.util;

public class ValidaInscricaoEstadual {

    public ValidaInscricaoEstadual() {
    }

    public boolean validaIE(String ie, String uf) {
        boolean retorno = false;
        String UF = "";
        if ((uf == null) || (uf.length() < 2)) return false;
        if (ie == null) return false;
        if (ie.equalsIgnoreCase("ISENTO")) return true;
        UF = uf.toUpperCase();
        if (UF.equals("AC")) retorno = validaAC(ie); else if (UF.equals("AL")) retorno = validaAL(ie); else if (UF.equals("AP")) retorno = validaAP(ie); else if (UF.equals("AM")) retorno = validaAM(ie); else if (UF.equals("BA")) retorno = validaBA(ie); else if (UF.equals("CE")) retorno = validaCE(ie); else if (UF.equals("DF")) retorno = validaDF(ie); else if (UF.equals("ES")) retorno = validaES(ie); else if (UF.equals("GO")) retorno = validaGO(ie); else if (UF.equals("MA")) retorno = validaMA(ie); else if (UF.equals("MT")) retorno = validaMT(ie); else if (UF.equals("MS")) retorno = validaMS(ie); else if (UF.equals("MG")) retorno = validaMG(ie); else if (UF.equals("PA")) retorno = validaPA(ie); else if (UF.equals("PB")) retorno = validaPB(ie); else if (UF.equals("PR")) retorno = validaPR(ie); else if (UF.equals("PE")) retorno = validaPE(ie); else if (UF.equals("PI")) retorno = validaPI(ie); else if (UF.equals("RJ")) retorno = validaRJ(ie); else if (UF.equals("RN")) retorno = validaRN(ie); else if (UF.equals("RS")) retorno = validaRS(ie); else if (UF.equals("RO")) retorno = validaRO(ie); else if (UF.equals("RR")) retorno = validaRR(ie); else if (UF.equals("SC")) retorno = validaSC(ie); else if (UF.equals("SP")) retorno = validaSP(ie); else if (UF.equals("SE")) retorno = validaSE(ie); else if (UF.equals("TO")) retorno = validaTO(ie); else retorno = false;
        return retorno;
    }

    public String MascaraIE(String ie, String uf) {
        return ie;
    }

    public String MascaraIE_Desativada(String ie, String uf) {
        String retorno = "";
        String UF = "";
        UF = uf.toUpperCase();
        if (ie.length() > 0) ie = ie.replace(".", "").replace("-", "").replace(",", "").replace("/", "").replace(" ", "").trim();
        if (UF.equals("AC")) if (ie.length() > 12) retorno = ie.substring(0, 2) + "." + ie.substring(2, 5) + "." + ie.substring(5, 8) + "/" + ie.substring(8, 11) + "-" + ie.substring(11, ie.length()); else retorno = ie; else if (UF.equals("AL")) retorno = ie; else if (UF.equals("AP")) retorno = ie; else if (UF.equals("AM")) if (ie.length() > 8) retorno = ie.substring(0, 2) + "." + ie.substring(2, 5) + "." + ie.substring(5, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("BA")) if (ie.length() > 7) retorno = ie.substring(0, 6) + "-" + ie.substring(6, ie.length()); else retorno = ie; else if (UF.equals("CE")) if (ie.length() > 8) retorno = ie.substring(0, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("DF")) if (ie.length() > 12) retorno = ie.substring(0, 11) + "-" + ie.substring(11, ie.length()); else retorno = ie; else if (UF.equals("ES")) retorno = ie; else if (UF.equals("GO")) if (ie.length() > 8) retorno = ie.substring(0, 2) + "." + ie.substring(2, 5) + "." + ie.substring(5, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("MA")) retorno = ie; else if (UF.equals("MT")) if (ie.length() > 10) retorno = ie.substring(0, 10) + "-" + ie.substring(10, ie.length()); else retorno = ie; else if (UF.equals("MS")) retorno = ie; else if (UF.equals("MG")) if (ie.length() > 12) retorno = ie.substring(0, 3) + "." + ie.substring(3, 6) + "." + ie.substring(6, 9) + "/" + ie.substring(9, ie.length()); else retorno = ie; else if (UF.equals("PA")) if (ie.length() > 8) retorno = ie.substring(0, 2) + "-" + ie.substring(2, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("PB")) if (ie.length() > 8) retorno = ie.substring(0, 8) + "-" + ie.substring(9, ie.length()); else retorno = ie; else if (UF.equals("PR")) if (ie.length() > 9) retorno = ie.substring(0, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("PE")) if (ie.length() > 13) retorno = ie.substring(0, 2) + "." + ie.substring(2, 3) + "." + ie.substring(3, 6) + "." + ie.substring(6, 13) + "-" + ie.substring(13, ie.length()); else retorno = ie; else if (UF.equals("PI")) retorno = ie; else if (UF.equals("RJ")) if (ie.length() > 7) retorno = ie.substring(0, 2) + "." + ie.substring(2, 5) + "." + ie.substring(5, 7) + "-" + ie.substring(7, ie.length()); else retorno = ie; else if (UF.equals("RN")) if (ie.length() == 9) retorno = ie.substring(0, 2) + "." + ie.substring(2, 5) + "." + ie.substring(5, 8) + "-" + ie.substring(8, ie.length()); else if (ie.length() == 10) retorno = ie.substring(0, 2) + "." + ie.substring(2, 3) + "." + ie.substring(3, 6) + "." + ie.substring(6, 9) + "-" + ie.substring(9, ie.length()); else retorno = ie; else if (UF.equals("RS")) if (ie.length() > 9) retorno = ie.substring(0, 3) + "/" + ie.substring(3, ie.length()); else retorno = ie; else if (UF.equals("RO")) if (ie.length() == 9) retorno = ie.substring(0, 3) + "." + ie.substring(3, 8) + "-" + ie.substring(8, ie.length()); else if (ie.length() == 14) retorno = ie.substring(0, 13) + "." + ie.substring(13, ie.length()); else retorno = ie; else if (UF.equals("RR")) if (ie.length() > 8) retorno = ie.substring(0, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("SC")) if (ie.length() > 8) retorno = ie.substring(0, 3) + "." + ie.substring(3, 6) + "." + ie.substring(6, ie.length()); else retorno = ie; else if (UF.equals("SP")) if (ie.length() == 12) retorno = ie.substring(0, 3) + "." + ie.substring(3, 6) + "." + ie.substring(6, 9) + "." + ie.substring(9, ie.length()); else if (ie.length() == 13) retorno = ie.substring(0, 1) + "-" + ie.substring(1, 9) + "." + ie.substring(9, 10) + "/" + ie.substring(10, ie.length()); else retorno = ie; else if (UF.equals("SE")) if (ie.length() > 8) retorno = ie.substring(0, 8) + "-" + ie.substring(8, ie.length()); else retorno = ie; else if (UF.equals("TO")) retorno = ie; else retorno = ie;
        if (retorno.length() == 0) {
            for (int ii = retorno.length(); ii < 16; ii++) {
                retorno = retorno.concat(" ");
            }
        }
        return retorno;
    }

    private boolean somenteNumeros(String numeros) {
        boolean retorno = true;
        if (numeros.length() < 0) return false;
        for (int i = 0; i < numeros.length(); i++) {
            if (!Character.isDigit(numeros.charAt(i))) {
                retorno = false;
                break;
            }
        }
        return retorno;
    }

    private boolean excessaoSP(String numeros) {
        boolean retorno = true;
        if (numeros.length() < 0) return false;
        for (int i = 0; i < numeros.length(); i++) {
            if (!Character.isLetterOrDigit(numeros.charAt(i))) {
                retorno = false;
                break;
            }
            if ((Character.isLetter(numeros.charAt(i))) && (numeros.charAt(i) != 'P')) {
                retorno = false;
                break;
            }
        }
        return retorno;
    }

    private int charToInt(char c) {
        int num = 0;
        num = c - 48;
        return num;
    }

    public boolean validaAC(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 13) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 4;
        soma = 0;
        for (int i = 0; i < 11; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(11)));
        if (!Result) return false;
        peso = 5;
        soma = 0;
        for (int i = 0; i < 12; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(12)));
        return Result;
    }

    public boolean validaAL(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("24")) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        soma = soma * 10;
        dig = soma - ((soma / 11) * 11);
        dig = (dig == 10) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaAP(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        int base = 0;
        int d = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("03")) return false;
        long faixa01 = Integer.valueOf(ie.substring(0, 8));
        long faixa02 = Integer.valueOf(ie.substring(0, 8));
        long faixa03 = Integer.valueOf(ie.substring(0, 8));
        if ((faixa01 >= 3000001) && (faixa01 <= 3017000)) {
            peso = 5;
            d = 0;
        }
        if ((faixa02 >= 3017001) && (faixa02 <= 3019022)) {
            peso = 9;
            d = 1;
        }
        if ((faixa03 >= 3019023)) {
            peso = 0;
            d = 0;
        }
        soma = peso;
        base = 9;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * base));
            base--;
            if (base == 1) base = 9;
        }
        dig = 11 - (soma % 11);
        if (dig == 10) dig = 0; else if (dig == 11) dig = d;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaAM(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        int aux = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        if (soma < 11) dig = (11 - soma); else {
            aux = (soma % 11);
            if (aux <= 1) dig = 0; else dig = 11 - aux;
        }
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaBA(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        int numInicial = 0;
        String ieAux = "";
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 8) return false;
        if (!somenteNumeros(ie)) return false;
        numInicial = Integer.parseInt(ie.substring(0, 1));
        if ((numInicial < 6) || (numInicial == 8)) {
            peso = 7;
            soma = 0;
            for (int i = 0; i < 6; i++) {
                soma = (soma + (charToInt(ie.charAt(i)) * peso));
                peso--;
                if (peso == 1) peso = 9;
            }
            dig = 10 - (soma % 10);
            dig = (dig == 10) ? 0 : dig;
            Result = (dig == charToInt(ie.charAt(7)));
            ieAux = ie.substring(0, 6).concat(String.valueOf(dig));
            if (!Result) return false;
            peso = 8;
            soma = 0;
            for (int i = 0; i < 7; i++) {
                soma = (soma + (charToInt(ieAux.charAt(i)) * peso));
                peso--;
                if (peso == 1) peso = 9;
            }
            dig = 10 - (soma % 10);
            Result = (dig == charToInt(ie.charAt(6)));
        } else {
            peso = 7;
            soma = 0;
            for (int i = 0; i < 6; i++) {
                soma = (soma + (charToInt(ie.charAt(i)) * peso));
                peso--;
                if (peso == 1) peso = 9;
            }
            dig = 11 - (soma % 11);
            dig = (dig == 10 || dig == 11) ? 0 : dig;
            Result = (dig == charToInt(ie.charAt(7)));
            ieAux = ie.substring(0, 6).concat(String.valueOf(dig));
            if (!Result) return false;
            peso = 8;
            soma = 0;
            for (int i = 0; i < 7; i++) {
                soma = (soma + (charToInt(ieAux.charAt(i)) * peso));
                peso--;
                if (peso == 1) peso = 9;
            }
            dig = 11 - (soma % 11);
            Result = (dig == charToInt(ie.charAt(6)));
        }
        return Result;
    }

    public boolean validaCE(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaDF(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 13) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("07")) return false;
        peso = 4;
        soma = 0;
        for (int i = 0; i < 11; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(11)));
        if (!Result) return false;
        peso = 5;
        soma = 0;
        for (int i = 0; i < 12; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(12)));
        return Result;
    }

    public boolean validaES(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = (soma % 11);
        dig = (dig < 2) ? 0 : 11 - dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaGO(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if ((ie.substring(0, 2).equals("10")) || (ie.substring(0, 2).equals("11")) || (ie.substring(0, 2).equals("15"))) {
            if ((ie.substring(0, 8).equals("11094402")) && ((0 == charToInt(ie.charAt(8))) || (1 == charToInt(ie.charAt(8))))) return true;
            peso = 9;
            soma = 0;
            for (int i = 0; i < 8; i++) {
                soma = (soma + (charToInt(ie.charAt(i)) * peso));
                peso--;
                if (peso == 1) peso = 9;
            }
            dig = (soma % 11);
            if (dig == 0) dig = 0; else if (dig == 1) {
                if ((Integer.parseInt(ie.substring(0, 8)) >= 10103105) && (Integer.parseInt(ie.substring(0, 8)) <= 10119997)) dig = 1; else dig = 0;
            } else dig = (11 - dig);
            Result = (dig == charToInt(ie.charAt(8)));
        } else {
            Result = false;
        }
        return Result;
    }

    public boolean validaMA(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("12")) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaMT(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() < 9) return false;
        if (!somenteNumeros(ie)) return false;
        for (int i = ie.length() - 1; i < 10; i++) {
            ie = "0".concat(ie);
        }
        peso = 3;
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(10)));
        return Result;
    }

    public boolean validaMS(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("28")) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaMG(String ie) {
        int peso = 0;
        int soma = 0;
        int somaNum = 0;
        int dig = 0;
        String aux = "";
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if ((ie.substring(0, 2).equals("PR")) && (ie.length() == 9)) return true;
        if (ie.length() != 13) return false;
        if (!somenteNumeros(ie)) return false;
        aux = ie.substring(0, 3).concat("0").concat(ie.substring(3));
        peso = 1;
        soma = 0;
        for (int i = 0; i < 12; i++) {
            soma = ((charToInt(aux.charAt(i)) * peso));
            if (soma >= 10) soma = soma - 9;
            somaNum = somaNum + soma;
            if (peso == 1) peso = 2; else peso = 1;
        }
        dig = 10 - (somaNum % 10);
        dig = (dig == 10) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(11)));
        if (!Result) return false;
        peso = 3;
        soma = 0;
        for (int i = 0; i < 12; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 11;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(12)));
        return Result;
    }

    public boolean validaPA(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("15")) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaPB(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaPI(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaRJ(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 8) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 2;
        soma = 0;
        for (int i = 0; i < 7; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 7;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(7)));
        return Result;
    }

    public boolean validaPR(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 10) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 3;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 7;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        if (!Result) return false;
        peso = 4;
        soma = 0;
        for (int i = 0; i < 9; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 7;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(9)));
        return Result;
    }

    public boolean validaPE(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 14) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 5;
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 0) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig > 9) ? (dig - 10) : dig;
        Result = (dig == charToInt(ie.charAt(13)));
        return Result;
    }

    public boolean validaRN(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (!((ie.length() > 8) && (ie.length() < 11))) return false;
        if (!somenteNumeros(ie)) return false;
        if (!ie.substring(0, 2).equals("20")) return false;
        peso = ie.length();
        soma = 0;
        for (int i = 0; i < (ie.length() - 1); i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = ((soma * 10) % 11);
        dig = (dig == 10) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(ie.length() - 1)));
        return Result;
    }

    public boolean validaRS(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 10) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 2;
        soma = 0;
        for (int i = 0; i < 9; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(9)));
        return Result;
    }

    public boolean validaRO(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() == 9) {
            String aux = ie.substring(3);
            for (int i = aux.length() - 1; i < 13; i++) {
                aux = "0".concat(aux);
            }
            ie = aux;
        }
        if (ie.length() != 14) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 6;
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig > 9) ? (dig - 10) : dig;
        Result = (dig == charToInt(ie.charAt(13)));
        return Result;
    }

    public boolean validaRR(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 1;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso++;
            if (peso == 10) peso = 0;
        }
        dig = (soma % 9);
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaSC(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaSP(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 12) if (ie.length() != 13) return false;
        if (!excessaoSP(ie)) return false;
        if (ie.length() == 12) {
            peso = 1;
            soma = 0;
            for (int i = 0; i < 8; i++) {
                soma = (soma + (charToInt(ie.charAt(i)) * peso));
                peso++;
                if (peso == 2) peso = 3;
                if (peso == 9) peso = 10;
            }
            dig = (soma % 11);
            dig = (dig > 9) ? 0 : dig;
            Result = (dig == charToInt(ie.charAt(8)));
            if (!Result) return false;
            peso = 3;
            soma = 0;
            for (int i = 0; i < 11; i++) {
                soma = (soma + (charToInt(ie.charAt(i)) * peso));
                peso--;
                if (peso == 1) peso = 10;
            }
            dig = (soma % 11);
            dig = (dig > 9) ? 0 : dig;
            Result = (dig == charToInt(ie.charAt(11)));
            if (!Result) return false;
        } else if (ie.length() == 13) {
            peso = 1;
            soma = 0;
            for (int i = 1; i < 9; i++) {
                soma = (soma + (charToInt(ie.charAt(i)) * peso));
                peso++;
                if (peso == 2) peso = 3;
                if (peso == 9) peso = 10;
            }
            dig = (soma % 11);
            dig = (dig > 9) ? 0 : dig;
            Result = (dig == charToInt(ie.charAt(9)));
            if (!Result) return false;
        }
        return Result;
    }

    public boolean validaSE(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = 11 - (soma % 11);
        dig = (dig == 10 || dig == 11) ? 0 : dig;
        Result = (dig == charToInt(ie.charAt(8)));
        return Result;
    }

    public boolean validaTO(String ie) {
        int peso = 0;
        int soma = 0;
        int dig = 0;
        boolean Result = false;
        if (ie.equals("")) return true;
        ie = ie.replace(".", "").replace("/", "").replace(",", "").replace("-", "").replace(" ", "").trim();
        if (ie.length() != 11) if (ie.length() != 9) return false;
        if (!somenteNumeros(ie)) return false;
        if (ie.length() == 11) ie = ie.substring(0, 2).concat(ie.substring(4));
        peso = 9;
        soma = 0;
        for (int i = 0; i < 8; i++) {
            soma = (soma + (charToInt(ie.charAt(i)) * peso));
            peso--;
            if (peso == 1) peso = 9;
        }
        dig = (soma % 11);
        dig = (dig < 2) ? 0 : (11 - dig);
        Result = (dig == charToInt(ie.charAt((ie.length() - 1))));
        return Result;
    }
}
