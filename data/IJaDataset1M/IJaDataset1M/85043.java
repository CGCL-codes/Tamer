package br.com.dip.validador;

import br.com.dip.entidade.ExameAnaminesio;
import br.com.dip.excecoes.ValidadorException;

public class ValidadorExameAnaminesio implements ValidadorPadrao {

    public void valida(Object obj) throws ValidadorException {
        ExameAnaminesio exame = (ExameAnaminesio) obj;
        if (exame.getCliente() == null || exame.getCliente().getId() == null) {
            throw new ValidadorException("Selecione um cliente");
        } else if (exame.getIdade() == null || exame.getIdade() <= 0) {
            throw new ValidadorException("Informe a idade do cliente");
        } else if (exame.getPeso() == null || exame.getPeso() <= 0) {
            throw new ValidadorException("O peso n�o pode ser em brando ou ser menor que 0");
        } else if (exame.getAltura() == null || exame.getAltura() <= 0) {
            throw new ValidadorException("Altura n�o pode ser em branco e nem menor que 0");
        } else if (exame.getIntencidadeAtividadesFisicasPraticada() == null || exame.getIntencidadeAtividadesFisicasPraticada() < 0) {
            throw new ValidadorException("Seleciona a Atividade");
        }
        if (exame.getTipoCalculoPorcentagemGordura() == null) {
        } else if (exame.getTipoCalculoPorcentagemGordura() == 0) {
            if (exame.getMedidaAdipometroPeito() == null || exame.getMedidaAdipometroPeito() <= 0) {
                throw new ValidadorException("Informe a medida do peito");
            } else if (exame.getMedidaAdipometroAbdomen() == null || exame.getMedidaAdipometroAbdomen() <= 0) {
                throw new ValidadorException("Informe a medida do abd�men");
            } else if (exame.getMedidaAdipometroPerna() == null || exame.getMedidaAdipometroPerna() <= 0) {
                throw new ValidadorException("Informe a medida do perna");
            }
        } else if (exame.getTipoCalculoPorcentagemGordura() == 1) {
            if (exame.getMedidaPescoco() == null || exame.getMedidaPescoco() <= 0) {
                throw new ValidadorException("Informe a medida do pesco�o");
            } else if (exame.getMedidaCintura() == null || exame.getMedidaCintura() <= 0) {
                throw new ValidadorException("Informe a medida da Cintura");
            } else if (exame.getMedidaQuadril() == null || exame.getMedidaQuadril() <= 0) {
                throw new ValidadorException("Informe a medida do Quadril");
            }
        } else if (exame.getTipoCalculoPorcentagemGordura() == 2) {
            if (exame.getMedidaAdipometroTriceps() == null || exame.getMedidaAdipometroTriceps() <= 0) {
                throw new ValidadorException("Informe a medida do Tr�ceps");
            } else if (exame.getMedidaAdipometroSubAxilar() == null || exame.getMedidaAdipometroSubAxilar() <= 0) {
                throw new ValidadorException("Informe a medida do Sub-Axilar");
            } else if (exame.getMedidaAdipometroSubEscapular() == null || exame.getMedidaAdipometroSubEscapular() <= 0) {
                throw new ValidadorException("Informe a medida do Sub-Axilar");
            } else if (exame.getMedidaAdipometroSupraIliaca() == null || exame.getMedidaAdipometroSupraIliaca() <= 0) {
                throw new ValidadorException("Informe a medida do Supra-il�aca");
            }
            if (exame.getMedidaAdipometroPeito() == null || exame.getMedidaAdipometroPeito() <= 0) {
                throw new ValidadorException("Informe a medida do peito");
            } else if (exame.getMedidaAdipometroAbdomen() == null || exame.getMedidaAdipometroAbdomen() <= 0) {
                throw new ValidadorException("Informe a medida do abd�men");
            } else if (exame.getMedidaAdipometroPerna() == null || exame.getMedidaAdipometroPerna() <= 0) {
                throw new ValidadorException("Informe a medida do perna");
            }
        }
        if (exame.getMedidaPescoco2() == null) {
            throw new ValidadorException("Informe a medida do pesco�o");
        } else if (exame.getOmbro() == null) {
            throw new ValidadorException("Informe a medida do ombro");
        } else if (exame.getBicepsDireito() == null) {
            throw new ValidadorException("Informe a medida do B�ceps Direito");
        } else if (exame.getBicepsEsquerdo() == null) {
            throw new ValidadorException("Informe a medida do B�ceps Esquerdo");
        } else if (exame.getAbdomen() == null) {
            throw new ValidadorException("Informe a medida do Abdomen");
        } else if (exame.getMedidaquadril() == null) {
            throw new ValidadorException("Informe a medida do quadril");
        } else if (exame.getCoxaDireita() == null) {
            throw new ValidadorException("Informe a medida do coxa direita");
        } else if (exame.getCoxaEsquerda() == null) {
            throw new ValidadorException("Informe a medida do coxa esquerda");
        }
    }
}
