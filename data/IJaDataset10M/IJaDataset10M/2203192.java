package jfan.io.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import jfan.fan.RedeFAN;
import jfan.fan.normalizadores.Normalizador;
import jfan.fan.normalizadores.NormalizadorConfig;
import jfan.fan.normalizadores.NormalizadorMaxMin;
import jfan.io.PersistorRede;

public class PersistorRedeJson implements PersistorRede {

    @Override
    public void persistir(RedeFAN rede, Normalizador normalizador, File arquivo) throws IOException {
        BufferedWriter bw = null;
        try {
            SerializadorJson ser = new SerializadorJson();
            String s = ser.serializar(rede);
            FileWriter fw = new FileWriter(arquivo);
            bw = new BufferedWriter(fw);
            bw.write(s);
            bw.newLine();
            s = ser.serializar(normalizador);
            bw.write(s);
            bw.close();
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public static void main(String[] args) {
        RedeFAN rede = new RedeFAN(2, 100, 2, 2);
        Normalizador norm = new NormalizadorMaxMin();
        norm.atualizarConfiguracao(new NormalizadorConfig());
        PersistorRedeJson p = new PersistorRedeJson();
        try {
            p.persistir(rede, norm, new File("/home/filipe/testeJson.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("fim");
    }
}
