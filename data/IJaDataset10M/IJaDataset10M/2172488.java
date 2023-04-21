package br.com.studies.designpattern.facade;

public class ClientFace {

    public static void main(String[] args) {
        FacadeSystens fs = new FacadeSystens();
        fs.executeSystemOne();
        fs.executeSystemTwo();
        fs.executeSystemThree();
    }
}
