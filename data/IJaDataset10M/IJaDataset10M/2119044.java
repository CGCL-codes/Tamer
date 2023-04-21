package update5.enumerations;

/**
 * F�cil como debugar thread!  
 * @author Fabr�cio Silva Epaminondas
 */
public class TheNewWay {

    enum Estacao {

        INVERNO, PRIMAVERA, VERAO, OUTONO
    }

    ;

    class Mes {

        private Estacao estacao = Estacao.INVERNO;

        public void setEstacao(Estacao estacao) {
            this.estacao = estacao;
        }

        public Estacao getEstacao() {
            return estacao;
        }
    }

    /**
	* � Tem mais: enums s�o como classes!
	* � Podem ter construtores, m�todos, etc.
	*/
    enum Cargo {

        GERENTE("Gerente"), SUPERVISOR("Supervisor"), ANALISTA("Analista"), PROGRAMADOR("Programador"), PE_RAPADO("Pe Rapado");

        private String nome;

        private Cargo(String name) {
            this.nome = name;
        }

        public String toString() {
            return nome;
        }
    }

    class Empregado {

        private Cargo cargo = null;

        public void setCargo(Cargo cargo) {
            this.cargo = cargo;
        }

        public Cargo getCargo() {
            return cargo;
        }
    }

    /**
	* Enums com polimorfismo:
	* � � poss�vel atribuir comportamentos diferentes a constantes
	* � Constant-specific methods: m�todo abstrato � implementado na chamada de cada constante
	*/
    public enum Operation {

        PLUS {

            double eval(double x, double y) {
                return x + y;
            }
        }
        , MINUS {

            double eval(double x, double y) {
                return x - y;
            }
        }
        , TIMES {

            double eval(double x, double y) {
                return x * y;
            }
        }
        , DIVIDE {

            double eval(double x, double y) {
                return x / y;
            }
        }
        ;

        abstract double eval(double x, double y);
    }
}
