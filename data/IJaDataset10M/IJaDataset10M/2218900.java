package models;

public class EstServicoLink {

    private int est_servico_id;

    private String nome;

    private EstServicoLink next;

    public EstServicoLink(int key_est_servico_id, String key_nome) {
        this.est_servico_id = key_est_servico_id;
        this.nome = key_nome;
    }

    public int getEstServicoId() {
        return this.est_servico_id;
    }

    public String getNome() {
        return this.nome;
    }

    public String toString() {
        return this.nome;
    }

    public EstServicoLink getNext() {
        return this.next;
    }

    public void setEstServicoId(int key_est_servico_id) {
        this.est_servico_id = key_est_servico_id;
    }

    public void setNome(String key_nome) {
        this.nome = key_nome;
    }

    public void setNext(EstServicoLink key_next) {
        this.next = key_next;
    }
}
