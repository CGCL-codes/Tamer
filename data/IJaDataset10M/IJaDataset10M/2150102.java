package gerenciadorescola.controller.pattern.command;

import gerenciadorescola.model.Aluno;
import gerenciadorescola.model.Escola;
import gerenciadorescola.model.Turma;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro Freitas
 */
public class ComandoCriaAluno implements Command {

    public Map execute(Map<String, String> entrada) {
        Map<String, String> saida = new HashMap();
        Escola escola = Escola.getInstance();
        Map<Integer, Turma> t = buscaTurma(entrada);
        Map<Integer, Aluno> alunos = new HashMap();
        if (buscaTurma(entrada) != null) {
            Aluno aluno = new Aluno();
            aluno.setNome(entrada.get("nome"));
            aluno.setCpf(entrada.get("cpf"));
            aluno.setTelefone(entrada.get("tel"));
            aluno.setEndereco(entrada.get("end"));
            aluno.setSexo(entrada.get("sexo"));
            aluno.setMatricula(Integer.parseInt(entrada.get("matricula")));
            aluno.setIdade(Integer.parseInt(entrada.get("idade")));
            aluno.setLingua(entrada.get("lingua"));
            aluno.setCodigo(escola.getContAlunos());
            escola.setContAlunos(escola.getContAlunos() + 1);
            alunos.put(aluno.getCodigo(), aluno);
            escola.salvar();
            t.get(0).setAlunos(alunos);
            saida.put("msg", "Aluno " + aluno.getCodigo() + " criado com sucesso!");
            System.out.println(aluno.toString());
        } else {
            saida.put("msg", "A turma não pode ser encontrada!");
        }
        return saida;
    }

    public Map<Integer, Turma> buscaTurma(Map<String, String> entrada) {
        Escola escola = Escola.getInstance();
        Map<Integer, Turma> turmas = escola.getTurmas();
        int codigo = Integer.parseInt(entrada.get("codTurma"));
        String lingua = entrada.get("lingua");
        for (Map.Entry<Integer, Turma> t : turmas.entrySet()) {
            if ((codigo == t.getKey()) && (t.getValue().getLingua().equals(lingua))) {
                turmas = new HashMap();
                turmas.put(0, t.getValue());
                return turmas;
            }
        }
        return null;
    }
}
