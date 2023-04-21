package br.org.rfccj.siap;

import java.io.Serializable;
import org.joda.time.LocalDate;

public class Retorno implements Serializable {

    private Paciente paciente;

    private LocalDate data;

    private MotivoDeRetorno motivo;

    public Retorno(Paciente paciente, LocalDate data, MotivoDeRetorno motivo) {
        this.paciente = paciente;
        this.data = data;
        this.motivo = motivo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDate getData() {
        return data;
    }

    public MotivoDeRetorno getMotivo() {
        return motivo;
    }
}
