package br.com.promove.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class InconsistenciaAvaria implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_erro_avaria", sequenceName = "seq_erro_avaria")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_erro_avaria")
    private Integer id;

    @ManyToOne(targetEntity = Veiculo.class)
    private Veiculo veiculo;

    @OneToOne
    private TipoAvaria tipo;

    @OneToOne
    private LocalAvaria local;

    @OneToOne
    private OrigemAvaria origem;

    @OneToOne
    private ExtensaoAvaria extensao;

    @OneToOne
    private Clima clima;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLancamento = new Date();

    @OneToOne
    private Usuario usuario;

    private String observacao;

    private String msgErro;

    private String chassiInvalido;

    private String hora;

    @OneToOne
    private NivelAvaria nivel;

    private String chassiGravado;

    private String etiqueta;

    private String autoDestrutivel;

    public InconsistenciaAvaria() {
    }

    public InconsistenciaAvaria(Avaria av, String msgErro) {
        this.veiculo = av.getVeiculo();
        this.tipo = av.getTipo();
        this.local = av.getLocal();
        this.origem = av.getOrigem();
        this.extensao = av.getExtensao();
        this.clima = av.getClima();
        this.dataLancamento = av.getDataLancamento();
        this.usuario = av.getUsuario();
        this.observacao = av.getObservacao();
        this.hora = av.getHora();
        this.nivel = av.getNivel();
        this.msgErro = msgErro;
        this.chassiGravado = av.getChassiGravado();
        this.etiqueta = av.getEtiqueta();
        this.autoDestrutivel = av.getAutoDestrutivel();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoAvaria getTipo() {
        return tipo;
    }

    public void setTipo(TipoAvaria tipo) {
        this.tipo = tipo;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalAvaria getLocal() {
        return local;
    }

    public void setLocal(LocalAvaria local) {
        this.local = local;
    }

    public OrigemAvaria getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemAvaria origem) {
        this.origem = origem;
    }

    public ExtensaoAvaria getExtensao() {
        return extensao;
    }

    public void setExtensao(ExtensaoAvaria extensao) {
        this.extensao = extensao;
    }

    public Clima getClima() {
        return clima;
    }

    public void setClima(Clima clima) {
        this.clima = clima;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public Avaria getAvaria() {
        Avaria av = new Avaria();
        av.setClima(clima);
        av.setDataLancamento(dataLancamento);
        av.setExtensao(extensao);
        av.setLocal(local);
        av.setObservacao(observacao);
        av.setOrigem(origem);
        av.setTipo(tipo);
        av.setUsuario(usuario);
        av.setVeiculo(veiculo);
        av.setHora(hora);
        av.setNivel(nivel);
        av.setChassiGravado(chassiGravado);
        av.setEtiqueta(etiqueta);
        av.setAutoDestrutivel(autoDestrutivel);
        return av;
    }

    public String getChassiInvalido() {
        return chassiInvalido;
    }

    public void setChassiInvalido(String chassiInvalido) {
        this.chassiInvalido = chassiInvalido;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

    public void setNivel(NivelAvaria nivel) {
        this.nivel = nivel;
    }

    public NivelAvaria getNivel() {
        return nivel;
    }

    public void setChassiGravado(String chassiGravado) {
        this.chassiGravado = chassiGravado;
    }

    public String getChassiGravado() {
        return chassiGravado;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setAutoDestrutivel(String autoDestrutivel) {
        this.autoDestrutivel = autoDestrutivel;
    }

    public String getAutoDestrutivel() {
        return autoDestrutivel;
    }
}
