package br.ufal.graw.whiteboard;

public class Rascunho {

    protected int tool;

    protected int rgb;

    protected int xyi;

    protected int xyf;

    protected String texto;

    public Rascunho(int tool, int rgb, int xyi, int xyf, String texto) {
        this.tool = tool;
        this.rgb = rgb;
        this.xyi = xyi;
        this.xyf = xyf;
        this.texto = texto;
    }
}
