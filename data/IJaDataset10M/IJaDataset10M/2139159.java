package br.org.sd.combo;

public class ComboException extends Exception {

    private static final long serialVersionUID = 7747084687195198307L;

    private String bean;

    public ComboException(String bean) {
        this.bean = bean;
    }

    public String getMessage() {
        return "Nao foi possivel criar um combo para a entidade " + bean;
    }
}
