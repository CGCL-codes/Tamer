package com.odontosis.testeprincipal;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;

public class TesteImpressaoFIOD {

    public static void imprimir() {
        Frame f = new Frame("Frame tempor�rio");
        f.pack();
        Toolkit tk = f.getToolkit();
        PrintJob pj = tk.getPrintJob(f, "print", null);
        if (pj != null) {
            for (int i = 0; i < 2; i++) {
                Graphics g = pj.getGraphics();
                g.setFont(new Font("Courier New", Font.PLAIN, 10));
                g.drawString("x", 83, 108);
                g.drawString("x", 83, 118);
                g.drawString("123.458.409-05", 180, 115);
                g.drawString("Dr. xxxx", 330, 115);
                g.drawString("     678", 50, 145);
                g.drawString("xxxx", 155, 145);
                g.drawString("xxxx", 335, 145);
                g.setFont(new Font("Courier New", Font.PLAIN, 6));
                g.drawString("Manuten��o Ortod�ntica Mensal-1 10/2009", 40, 195);
                g.drawString("Tratamento Ortod�ntico Corretivo", 40, 210);
                g.drawString("Tratamento Ortod�ntico Corretivo (Extens�o)", 40, 225);
                g.drawString("Aparelho Ortod�ntico Bimler/Frankel/Planas/Bionator/Monobloco ou Similar", 50, 240);
                g.drawString("teste tratamento5", 50, 255);
                g.drawString("teste tratamento6", 335, 195);
                g.drawString("teste tratamento7", 335, 210);
                g.drawString("teste tratamento8", 335, 225);
                g.drawString("teste tratamento9", 335, 240);
                g.drawString("teste tratamento10", 335, 255);
                g.setFont(new Font("Courier New", Font.PLAIN, 10));
                g.drawString("v1", 245, 195);
                g.drawString("v2", 245, 210);
                g.drawString("v3", 245, 225);
                g.drawString("v4", 245, 240);
                g.drawString("v5", 245, 255);
                g.drawString("v6", 530, 195);
                g.drawString("v7", 530, 210);
                g.drawString("v8", 530, 225);
                g.drawString("v9", 530, 240);
                g.drawString("v10", 530, 255);
                g.drawString("total", 30, 285);
                g.drawString("profissional solicitante", 30, 310);
                g.drawString("cro", 445, 310);
                g.drawString("00/00/0000 15:30", 30, 345);
                g.drawString("123.456.789-09", 340, 310);
                g.drawString("MG", 550, 305);
                g.drawString("fim", 500, 810);
                g.dispose();
            }
            pj.end();
        }
        f.dispose();
    }

    public static void main(String[] args) {
        imprimir();
    }
}
