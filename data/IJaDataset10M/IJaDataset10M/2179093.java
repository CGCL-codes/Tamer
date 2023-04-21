package calculadoraagil.model;

public class Calculadora {

    private String entrada;

    private int estado;

    private String entradaDecimal;

    private double acumulador;

    private int operacao;

    private int resultado;

    public Calculadora() {
        this.limpar();
    }

    public String limpar() {
        this.entrada = "0";
        this.estado = 0;
        this.entradaDecimal = "";
        this.acumulador = 0;
        return this.entrada;
    }

    public String limparEntrada() {
        this.entrada = "0";
        this.entradaDecimal = "";
        return this.entrada;
    }

    public String armazenarValorNaMemoria() {
        return "";
    }

    public String entrarZero() {
        return this.entrarDigito("0");
    }

    public String entrarUm() {
        return this.entrarDigito("1");
    }

    public String entrarDois() {
        return this.entrarDigito("2");
    }

    public String entrarTres() {
        return this.entrarDigito("3");
    }

    public String entrarQuatro() {
        return this.entrarDigito("4");
    }

    public String entrarCinco() {
        return this.entrarDigito("5");
    }

    public String entrarSeis() {
        return this.entrarDigito("6");
    }

    public String entrarSete() {
        return this.entrarDigito("7");
    }

    public String entrarOito() {
        return this.entrarDigito("8");
    }

    public String entrarNove() {
        return this.entrarDigito("9");
    }

    public String entrarPonto() {
        this.estado = 1;
        return entrarDigito("");
    }

    String setEntrada(String s) {
        this.entrada = s;
        return s;
    }

    private String entrarDigito(String digito) {
        String res = "";
        switch(this.estado) {
            case 1:
                this.entradaDecimal += digito;
                res = String.valueOf(Integer.parseInt(this.entrada)) + "." + this.entradaDecimal;
                if (digito.equals("0")) {
                    return res;
                }
                break;
            default:
                this.entrada += digito;
                res = this.entrada;
                break;
        }
        return this.pegarNumeroSemZeronoFinal(String.valueOf(Double.parseDouble(res)));
    }

    public String efetuarOperacao(int operacao) {
        this.operacao = operacao;
        switch(operacao) {
            case 1:
                this.acumulador += Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                break;
            case 2:
                if (this.acumulador == 0) {
                    this.acumulador = Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                } else {
                    this.acumulador -= Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                }
                break;
            case 3:
                if (this.acumulador == 0) {
                    this.acumulador = Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                } else {
                    this.acumulador *= Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                }
                break;
            case 4:
                if (this.acumulador == 0) {
                    this.acumulador = Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                } else {
                    this.acumulador /= Double.parseDouble(this.entrada + "." + this.entradaDecimal);
                }
                break;
            default:
                break;
        }
        this.entrada = "0";
        this.entradaDecimal = "";
        this.estado = 0;
        return this.pegarNumeroSemZeronoFinal(String.valueOf(this.acumulador));
    }

    public String somar() {
        return this.efetuarOperacao(1);
    }

    public String subtrair() {
        return this.efetuarOperacao(2);
    }

    public String multiplicar() {
        return this.efetuarOperacao(3);
    }

    public String dividir() {
        return this.efetuarOperacao(4);
    }

    private String pegarNumeroSemZeronoFinal(String number) {
        if (number.toCharArray()[number.length() - 2] != '.' && number.toCharArray()[number.length() - 1] == '0') {
            return number.substring(0, number.length() - 1);
        }
        return number;
    }

    public String apagarDigito() {
        String resposta = "";
        if (!(entradaDecimal.equals(""))) {
            this.entradaDecimal = this.entradaDecimal.substring(0, (this.entradaDecimal.length() - 1));
            resposta = this.entrada + "." + this.entradaDecimal;
        } else if (estado == 1) {
            estado = 0;
            resposta = this.entrada;
        } else {
            if (this.entrada.length() == 1) {
                this.entrada = "0";
            } else {
                this.entrada = this.entrada.substring(0, (this.entrada.length() - 1));
            }
            resposta = this.entrada;
        }
        return String.valueOf(Double.parseDouble(resposta));
    }

    public String raizQuadrada() {
        if (this.acumulador != 0) {
            this.acumulador = Math.sqrt(this.acumulador);
            return this.pegarNumeroSemZeronoFinal(String.valueOf(this.acumulador));
        } else {
            this.acumulador = Math.sqrt(Double.parseDouble(this.entrada + "." + this.entradaDecimal));
            return this.pegarNumeroSemZeronoFinal(String.valueOf(this.acumulador));
        }
    }

    public String igual() {
        return efetuarOperacao(this.operacao);
    }
}
