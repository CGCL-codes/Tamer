package br.ufrj.cad.view.disciplinaministrada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.struts.action.ActionForm;
import br.ufrj.cad.fwk.util.Util;
import br.ufrj.cad.model.bo.Disciplina;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.bo.UsuarioComum;

public class DisciplinaMinistradaForm extends ActionForm {

    private static final long serialVersionUID = 4301443842171485251L;

    private List<Usuario> usuarios = new ArrayList<Usuario>();

    private Map<String, String> disciplinasMinisradas = new HashMap<String, String>();

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Map<String, String> getDisciplinasMinisradas() {
        return disciplinasMinisradas;
    }

    public void setDisciplinasMinisradas(Map<String, String> disciplinasMinisradas) {
        this.disciplinasMinisradas = disciplinasMinisradas;
    }

    /**
     * Preenche os dados internos do form com as informações recebidas.
     * @param usuarios2
     * @param mapDisciplinasUsuario
     */
    public void preencherForm(List<Usuario> usuarios, Map<String, List<Disciplina>> mapDisciplinasUsuario) {
        this.usuarios = usuarios;
        for (Usuario usuario : usuarios) {
            List<Disciplina> disciplinasDoUsuario = mapDisciplinasUsuario.get(usuario.getId().toString());
            if (Util.preenchido(disciplinasDoUsuario)) {
                for (int i = 0; i < disciplinasDoUsuario.size(); i++) {
                    this.disciplinasMinisradas.put(usuario.getId().toString() + "-" + i, disciplinasDoUsuario.get(i).getId().toString());
                }
            }
        }
    }

    /**
     * Retorna um map com a lista de disciplinas de cada usuario.
     * a key da hasm é o id do usuario
     * @return
     */
    public Map<String, List<Disciplina>> retornarDisciplinasPorUsuario() {
        Map<String, List<Disciplina>> disciplinas = new HashMap<String, List<Disciplina>>();
        List<UsuarioComum> listaDeUsuarios = retornarUsuarios();
        for (UsuarioComum usuarioComum : listaDeUsuarios) {
            List<Disciplina> disciplinasDoUsuario = new ArrayList<Disciplina>();
            for (int i = 0; i < 6; i++) {
                String idEenesimaDisciplina = this.disciplinasMinisradas.get(montaKey(usuarioComum, i));
                if (Util.preenchido(idEenesimaDisciplina)) {
                    Disciplina disciplina = new Disciplina();
                    disciplina.setId(new Long(idEenesimaDisciplina));
                    disciplinasDoUsuario.add(disciplina);
                }
            }
            disciplinas.put(usuarioComum.getId().toString(), disciplinasDoUsuario);
        }
        return disciplinas;
    }

    private String montaKey(UsuarioComum usuarioComum, int i) {
        return usuarioComum.getId().toString() + "-" + i;
    }

    /**
     * Retorna a lista de usuarios que possuem disciplinas ministradas
     * @return
     */
    public List<UsuarioComum> retornarUsuarios() {
        List<UsuarioComum> usuarios = new ArrayList<UsuarioComum>();
        Map<String, String> mapUsuarios = new HashMap<String, String>();
        Set<String> keySetDisciplinas = this.disciplinasMinisradas.keySet();
        Iterator<String> iterador = keySetDisciplinas.iterator();
        while (iterador.hasNext()) {
            String key = iterador.next();
            String idUsuario = retornarIdUsuario(key);
            mapUsuarios.put(idUsuario, idUsuario);
        }
        Iterator<String> iteradorUsuarios = mapUsuarios.keySet().iterator();
        while (iteradorUsuarios.hasNext()) {
            UsuarioComum usuario = new UsuarioComum();
            usuario.setId(new Long(iteradorUsuarios.next()));
            usuarios.add(usuario);
        }
        return usuarios;
    }

    /**
     * Dada uma chave do hashmap, retorna o id do usuario
     * 
     * @param key
     * @return
     */
    private String retornarIdUsuario(String key) {
        return key.split("-")[0];
    }
}
