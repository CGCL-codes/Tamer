package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the tbproduto_itens_copy database table.
 * 
 */
@Entity
@Table(name = "tbproduto_itens_copy")
public class TbprodutoItensCopy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    private double custo;

    private double custofiscal;

    @Temporal(TemporalType.DATE)
    private Date dataalteracao;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date datacadastro;

    @Column(length = 200)
    private String descricao;

    @Lob()
    private String flag;

    @Lob()
    private String flag2;

    private int idcarac1;

    private int idcarac10;

    private int idcarac11;

    private int idcarac12;

    private int idcarac13;

    private int idcarac14;

    private int idcarac15;

    private int idcarac16;

    private int idcarac17;

    private int idcarac18;

    private int idcarac19;

    private int idcarac2;

    private int idcarac20;

    private int idcarac3;

    private int idcarac4;

    private int idcarac5;

    private int idcarac6;

    private int idcarac7;

    private int idcarac8;

    private int idcarac9;

    private int idclasse;

    private int idcod;

    @Column(length = 50)
    private String idcod1;

    @Column(length = 50)
    private String idcod10;

    @Column(length = 50)
    private String idcod11;

    @Column(length = 50)
    private String idcod12;

    @Column(length = 50)
    private String idcod13;

    @Column(length = 50)
    private String idcod14;

    @Column(length = 50)
    private String idcod15;

    @Column(length = 50)
    private String idcod16;

    private int idcod17;

    @Column(length = 50)
    private String idcod18;

    @Column(length = 50)
    private String idcod19;

    @Column(length = 50)
    private String idcod2;

    @Column(length = 50)
    private String idcod20;

    @Column(length = 50)
    private String idcod3;

    @Column(length = 50)
    private String idcod4;

    @Column(length = 50)
    private String idcod5;

    @Column(length = 50)
    private String idcod6;

    @Column(length = 50)
    private String idcod7;

    @Column(length = 50)
    private String idcod8;

    @Column(length = 50)
    private String idcod9;

    private int idproduto;

    private double indice;

    private double margem;

    private double pedras;

    private double peso;

    private double pesox;

    @Lob()
    @Column(name = "ult_atu")
    private String ultAtu;

    @Column(length = 100)
    private String valor1;

    @Column(length = 100)
    private String valor10;

    @Column(length = 100)
    private String valor11;

    @Column(length = 100)
    private String valor12;

    @Column(length = 100)
    private String valor13;

    @Column(length = 100)
    private String valor14;

    @Column(length = 100)
    private String valor15;

    @Column(length = 100)
    private String valor16;

    @Column(length = 100)
    private String valor17;

    @Column(length = 100)
    private String valor18;

    @Column(length = 100)
    private String valor19;

    @Column(length = 100)
    private String valor2;

    @Column(length = 100)
    private String valor20;

    @Column(length = 100)
    private String valor3;

    @Column(length = 100)
    private String valor4;

    @Column(length = 100)
    private String valor5;

    @Column(length = 100)
    private String valor6;

    @Column(length = 100)
    private String valor7;

    @Column(length = 100)
    private String valor8;

    @Column(length = 100)
    private String valor9;

    private double venda;

    public TbprodutoItensCopy() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCusto() {
        return this.custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public double getCustofiscal() {
        return this.custofiscal;
    }

    public void setCustofiscal(double custofiscal) {
        this.custofiscal = custofiscal;
    }

    public Date getDataalteracao() {
        return this.dataalteracao;
    }

    public void setDataalteracao(Date dataalteracao) {
        this.dataalteracao = dataalteracao;
    }

    public Date getDatacadastro() {
        return this.datacadastro;
    }

    public void setDatacadastro(Date datacadastro) {
        this.datacadastro = datacadastro;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag2() {
        return this.flag2;
    }

    public void setFlag2(String flag2) {
        this.flag2 = flag2;
    }

    public int getIdcarac1() {
        return this.idcarac1;
    }

    public void setIdcarac1(int idcarac1) {
        this.idcarac1 = idcarac1;
    }

    public int getIdcarac10() {
        return this.idcarac10;
    }

    public void setIdcarac10(int idcarac10) {
        this.idcarac10 = idcarac10;
    }

    public int getIdcarac11() {
        return this.idcarac11;
    }

    public void setIdcarac11(int idcarac11) {
        this.idcarac11 = idcarac11;
    }

    public int getIdcarac12() {
        return this.idcarac12;
    }

    public void setIdcarac12(int idcarac12) {
        this.idcarac12 = idcarac12;
    }

    public int getIdcarac13() {
        return this.idcarac13;
    }

    public void setIdcarac13(int idcarac13) {
        this.idcarac13 = idcarac13;
    }

    public int getIdcarac14() {
        return this.idcarac14;
    }

    public void setIdcarac14(int idcarac14) {
        this.idcarac14 = idcarac14;
    }

    public int getIdcarac15() {
        return this.idcarac15;
    }

    public void setIdcarac15(int idcarac15) {
        this.idcarac15 = idcarac15;
    }

    public int getIdcarac16() {
        return this.idcarac16;
    }

    public void setIdcarac16(int idcarac16) {
        this.idcarac16 = idcarac16;
    }

    public int getIdcarac17() {
        return this.idcarac17;
    }

    public void setIdcarac17(int idcarac17) {
        this.idcarac17 = idcarac17;
    }

    public int getIdcarac18() {
        return this.idcarac18;
    }

    public void setIdcarac18(int idcarac18) {
        this.idcarac18 = idcarac18;
    }

    public int getIdcarac19() {
        return this.idcarac19;
    }

    public void setIdcarac19(int idcarac19) {
        this.idcarac19 = idcarac19;
    }

    public int getIdcarac2() {
        return this.idcarac2;
    }

    public void setIdcarac2(int idcarac2) {
        this.idcarac2 = idcarac2;
    }

    public int getIdcarac20() {
        return this.idcarac20;
    }

    public void setIdcarac20(int idcarac20) {
        this.idcarac20 = idcarac20;
    }

    public int getIdcarac3() {
        return this.idcarac3;
    }

    public void setIdcarac3(int idcarac3) {
        this.idcarac3 = idcarac3;
    }

    public int getIdcarac4() {
        return this.idcarac4;
    }

    public void setIdcarac4(int idcarac4) {
        this.idcarac4 = idcarac4;
    }

    public int getIdcarac5() {
        return this.idcarac5;
    }

    public void setIdcarac5(int idcarac5) {
        this.idcarac5 = idcarac5;
    }

    public int getIdcarac6() {
        return this.idcarac6;
    }

    public void setIdcarac6(int idcarac6) {
        this.idcarac6 = idcarac6;
    }

    public int getIdcarac7() {
        return this.idcarac7;
    }

    public void setIdcarac7(int idcarac7) {
        this.idcarac7 = idcarac7;
    }

    public int getIdcarac8() {
        return this.idcarac8;
    }

    public void setIdcarac8(int idcarac8) {
        this.idcarac8 = idcarac8;
    }

    public int getIdcarac9() {
        return this.idcarac9;
    }

    public void setIdcarac9(int idcarac9) {
        this.idcarac9 = idcarac9;
    }

    public int getIdclasse() {
        return this.idclasse;
    }

    public void setIdclasse(int idclasse) {
        this.idclasse = idclasse;
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public String getIdcod1() {
        return this.idcod1;
    }

    public void setIdcod1(String idcod1) {
        this.idcod1 = idcod1;
    }

    public String getIdcod10() {
        return this.idcod10;
    }

    public void setIdcod10(String idcod10) {
        this.idcod10 = idcod10;
    }

    public String getIdcod11() {
        return this.idcod11;
    }

    public void setIdcod11(String idcod11) {
        this.idcod11 = idcod11;
    }

    public String getIdcod12() {
        return this.idcod12;
    }

    public void setIdcod12(String idcod12) {
        this.idcod12 = idcod12;
    }

    public String getIdcod13() {
        return this.idcod13;
    }

    public void setIdcod13(String idcod13) {
        this.idcod13 = idcod13;
    }

    public String getIdcod14() {
        return this.idcod14;
    }

    public void setIdcod14(String idcod14) {
        this.idcod14 = idcod14;
    }

    public String getIdcod15() {
        return this.idcod15;
    }

    public void setIdcod15(String idcod15) {
        this.idcod15 = idcod15;
    }

    public String getIdcod16() {
        return this.idcod16;
    }

    public void setIdcod16(String idcod16) {
        this.idcod16 = idcod16;
    }

    public int getIdcod17() {
        return this.idcod17;
    }

    public void setIdcod17(int idcod17) {
        this.idcod17 = idcod17;
    }

    public String getIdcod18() {
        return this.idcod18;
    }

    public void setIdcod18(String idcod18) {
        this.idcod18 = idcod18;
    }

    public String getIdcod19() {
        return this.idcod19;
    }

    public void setIdcod19(String idcod19) {
        this.idcod19 = idcod19;
    }

    public String getIdcod2() {
        return this.idcod2;
    }

    public void setIdcod2(String idcod2) {
        this.idcod2 = idcod2;
    }

    public String getIdcod20() {
        return this.idcod20;
    }

    public void setIdcod20(String idcod20) {
        this.idcod20 = idcod20;
    }

    public String getIdcod3() {
        return this.idcod3;
    }

    public void setIdcod3(String idcod3) {
        this.idcod3 = idcod3;
    }

    public String getIdcod4() {
        return this.idcod4;
    }

    public void setIdcod4(String idcod4) {
        this.idcod4 = idcod4;
    }

    public String getIdcod5() {
        return this.idcod5;
    }

    public void setIdcod5(String idcod5) {
        this.idcod5 = idcod5;
    }

    public String getIdcod6() {
        return this.idcod6;
    }

    public void setIdcod6(String idcod6) {
        this.idcod6 = idcod6;
    }

    public String getIdcod7() {
        return this.idcod7;
    }

    public void setIdcod7(String idcod7) {
        this.idcod7 = idcod7;
    }

    public String getIdcod8() {
        return this.idcod8;
    }

    public void setIdcod8(String idcod8) {
        this.idcod8 = idcod8;
    }

    public String getIdcod9() {
        return this.idcod9;
    }

    public void setIdcod9(String idcod9) {
        this.idcod9 = idcod9;
    }

    public int getIdproduto() {
        return this.idproduto;
    }

    public void setIdproduto(int idproduto) {
        this.idproduto = idproduto;
    }

    public double getIndice() {
        return this.indice;
    }

    public void setIndice(double indice) {
        this.indice = indice;
    }

    public double getMargem() {
        return this.margem;
    }

    public void setMargem(double margem) {
        this.margem = margem;
    }

    public double getPedras() {
        return this.pedras;
    }

    public void setPedras(double pedras) {
        this.pedras = pedras;
    }

    public double getPeso() {
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getPesox() {
        return this.pesox;
    }

    public void setPesox(double pesox) {
        this.pesox = pesox;
    }

    public String getUltAtu() {
        return this.ultAtu;
    }

    public void setUltAtu(String ultAtu) {
        this.ultAtu = ultAtu;
    }

    public String getValor1() {
        return this.valor1;
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getValor10() {
        return this.valor10;
    }

    public void setValor10(String valor10) {
        this.valor10 = valor10;
    }

    public String getValor11() {
        return this.valor11;
    }

    public void setValor11(String valor11) {
        this.valor11 = valor11;
    }

    public String getValor12() {
        return this.valor12;
    }

    public void setValor12(String valor12) {
        this.valor12 = valor12;
    }

    public String getValor13() {
        return this.valor13;
    }

    public void setValor13(String valor13) {
        this.valor13 = valor13;
    }

    public String getValor14() {
        return this.valor14;
    }

    public void setValor14(String valor14) {
        this.valor14 = valor14;
    }

    public String getValor15() {
        return this.valor15;
    }

    public void setValor15(String valor15) {
        this.valor15 = valor15;
    }

    public String getValor16() {
        return this.valor16;
    }

    public void setValor16(String valor16) {
        this.valor16 = valor16;
    }

    public String getValor17() {
        return this.valor17;
    }

    public void setValor17(String valor17) {
        this.valor17 = valor17;
    }

    public String getValor18() {
        return this.valor18;
    }

    public void setValor18(String valor18) {
        this.valor18 = valor18;
    }

    public String getValor19() {
        return this.valor19;
    }

    public void setValor19(String valor19) {
        this.valor19 = valor19;
    }

    public String getValor2() {
        return this.valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getValor20() {
        return this.valor20;
    }

    public void setValor20(String valor20) {
        this.valor20 = valor20;
    }

    public String getValor3() {
        return this.valor3;
    }

    public void setValor3(String valor3) {
        this.valor3 = valor3;
    }

    public String getValor4() {
        return this.valor4;
    }

    public void setValor4(String valor4) {
        this.valor4 = valor4;
    }

    public String getValor5() {
        return this.valor5;
    }

    public void setValor5(String valor5) {
        this.valor5 = valor5;
    }

    public String getValor6() {
        return this.valor6;
    }

    public void setValor6(String valor6) {
        this.valor6 = valor6;
    }

    public String getValor7() {
        return this.valor7;
    }

    public void setValor7(String valor7) {
        this.valor7 = valor7;
    }

    public String getValor8() {
        return this.valor8;
    }

    public void setValor8(String valor8) {
        this.valor8 = valor8;
    }

    public String getValor9() {
        return this.valor9;
    }

    public void setValor9(String valor9) {
        this.valor9 = valor9;
    }

    public double getVenda() {
        return this.venda;
    }

    public void setVenda(double venda) {
        this.venda = venda;
    }
}
