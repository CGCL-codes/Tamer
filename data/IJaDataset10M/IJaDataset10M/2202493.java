package modelo.coche;

public class Coche {

    String matricula;

    Motor motor;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public String arranca() {
        return motor.getMarca();
    }
}
