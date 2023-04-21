package fr.insee.sicape.service;

import java.util.HashMap;
import java.util.Map;
import fr.insee.sicape.bean.Apet;
import fr.insee.sicape.bean.Echo;
import fr.insee.sicape.bean.Reponse;

public class ReponseService {

    private static ReponseService instance = null;

    private ReponseService() {
    }

    public static ReponseService getInstance() {
        if (instance == null) {
            instance = new ReponseService();
        }
        return instance;
    }

    public String getMeilleurCode(Reponse reponse, int seuil, int min, int max) {
        String code = null;
        for (int digits = max; digits >= min; digits--) {
            if ((code = this.getMeilleurCode(reponse, seuil, digits)) != null) {
                return code;
            }
        }
        return code;
    }

    public String getMeilleurCode(Reponse reponse, int seuil, int digits) {
        String code = null;
        int freq = 0;
        Map<String, Integer> codesFreq = this.getCodesFreq(reponse, digits);
        Map<String, Integer> codesPct = this.getCodesPct(reponse, digits);
        for (String apet : codesFreq.keySet()) {
            int f = codesFreq.get(apet);
            if (codesFreq.get(apet) > freq) {
                code = apet;
                freq = f;
            }
        }
        if (code != null && codesPct.get(code) > seuil) {
            return code;
        }
        return null;
    }

    public Map<String, Integer> getCodesFreq(Reponse reponse, int digits) {
        Map<String, Integer> codesFreq = new HashMap<String, Integer>(10);
        for (Echo echo : reponse.getEchos()) {
            for (Apet apet : echo) {
                Integer freq = 0;
                String code = apet.getCode().substring(0, digits);
                if (codesFreq.containsKey(code)) {
                    freq = codesFreq.get(code) + (echo.getFrequence5() * apet.getPourcentage()) / 100;
                } else {
                    freq = (echo.getFrequence5() * apet.getPourcentage()) / 100;
                }
                codesFreq.put(code, freq);
            }
        }
        return codesFreq;
    }

    public Map<String, Integer> getCodesPct(Reponse reponse, int digits) {
        Map<String, Integer> codesPct = new HashMap<String, Integer>(10);
        int total = 0;
        for (Echo echo : reponse.getEchos()) {
            total = total + echo.getFrequence5();
        }
        Map<String, Integer> codesFreq = getCodesFreq(reponse, digits);
        for (String code : codesFreq.keySet()) {
            codesPct.put(code, (codesFreq.get(code) * 100) / total);
        }
        return codesPct;
    }
}
