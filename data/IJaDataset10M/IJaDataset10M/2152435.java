package control.report.sistoc;

import java.util.List;
import model.business.Item;
import model.business.ItemOc;
import model.business.MaterialNecessario;
import model.business.Orcamento;
import model.business.Ordem;
import model.business.Tolerancia;
import control.report.ExcelHandler;
import control.report.ReportHandler;

public class OrcamentoPaginaInicial implements ReportUpdateStrategy {

    private Integer numerofolhas;

    public void Update(Ordem o, ExcelHandler handler) {
        boolean afrio = (o.kind == 1);
        Orcamento oc = (Orcamento) o;
        String text = "";
        handler.addString(10, 10, "p�g: 1/1");
        if (afrio) {
            text = "CF-" + o.getNumero() + " Rev." + oc.getRevisao();
            handler.addString(10, 4, text);
        } else {
            text = "C-" + o.getNumero() + " Rev." + oc.getRevisao();
            handler.addString(10, 4, text);
        }
        handler.addString(10, 8, oc.getDataEntrada().toString());
        handler.addString(4, 1, "PARA: " + oc.getCliente());
        handler.addString(5, 1, "AT: " + oc.getAtencao());
        handler.addString(6, 1, "SETOR: " + oc.getSetor());
        handler.addString(7, 1, "FAX N�: " + oc.getFax());
        handler.addString(8, 1, "Tel N�: " + oc.getTelefone());
        handler.addString(9, 1, "REF.: " + oc.getReferencia());
        handler.addString(5, 5, oc.getEmitente());
        Integer ContaCompl = 1;
        handler.addString(12, 1, ContaCompl + ") Material destinado a:");
        if (oc.getUtilizacao() == 0) {
            handler.addString(12, 3, "Consumo");
        } else {
            handler.addString(12, 3, "Revenda/Reindustrializa��o");
        }
        ContaCompl = ContaCompl + 1;
        handler.addString(13, 1, ContaCompl + ") Fornecimento do Material:");
        if (oc.getFornecimentoMaterial() == 0) {
            handler.addString(13, 3, "Cliente");
        } else {
            handler.addString(13, 3, "Protubo");
        }
        ContaCompl = ContaCompl + 1;
        handler.addString(14, 1, ContaCompl + ") Impostos:");
        Float imposto = oc.getImposto();
        Short impostohelper = oc.getImpostoOption();
        if (impostohelper == 0) {
            handler.addString(14, 4, "(Incluso)");
        } else if (impostohelper == 1) {
            handler.addString(14, 4, "(N�o Incluso)");
        } else {
            handler.addString(14, 2, "ICMS/ISS");
            handler.addString(14, 4, "(N�o Incluso)");
        }
        impostohelper = oc.getImpostoKind();
        if (impostohelper == 0) {
            handler.addString(14, 2, "ICMS");
        } else if (impostohelper == 1) {
            handler.addString(14, 2, "ISS");
        }
        handler.addString(14, 3, imposto + "%");
        handler.addString(15, 2, "IPI");
        imposto = oc.getIpi();
        impostohelper = oc.getIpiOption();
        if (impostohelper == 0) {
            handler.addString(15, 4, "(Incluso)");
        } else if (impostohelper == 1) {
            handler.addString(15, 4, "(N�o Incluso)");
        }
        handler.addString(15, 3, imposto + "%");
        Integer linha = 16;
        if (oc.getCondicaoPagamentoOption() == 0 || oc.getCondicaoPagamentoOption() == 1) handler.addString(linha, 1, ContaCompl + ") Condi��es de pagamento:");
        if (oc.getCondicaoPagamentoOption() == 0) {
            handler.addString(linha, 3, "- � vista: BANCO DO BRASIL Ag. 1251-3 / C.C. 103.719-6");
            linha = linha + 1;
        } else {
            if (oc.getCondicaoPagamentoOption() == 0) {
                handler.addString(linha, 3, "- 28 ddl " + "+ " + oc.getCondicaoPagamento() + " (MEDIANTE APROVA��O DE CADASTRO)");
            } else {
                handler.addString(linha, 3, "- 28 ddl " + "+ " + oc.getCondicaoPagamento() + " (MEDIANTE APROVA��O DE CADASTRO).");
            }
        }
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Validade da proposta:");
        handler.addString(linha, 3, oc.getValidade().toString());
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Prazo m�ximo de entrega:");
        handler.addString(linha, 3, oc.getPrazo().toString());
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Responsabilidade do transporte:");
        if (oc.getTransporte() == 0) {
            handler.addString(linha, 3, oc.getCliente());
        } else {
            handler.addString(linha, 3, "PROTUBO");
        }
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Embalagem:");
        if (oc.getEmbalagem() == 0) {
            handler.addString(linha, 3, "Inclusa");
        } else {
            handler.addString(linha, 3, "N�o Inclusa");
        }
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Trechos Retos:");
        if (oc.getTrechosRetosOption() == 0) {
            handler.addString(linha, 3, "No m�nimo conforme desenho enviado");
        } else if (oc.getTrechosRetosOption() == 1) {
            handler.addString(linha, 3, "M�nimo " + oc.getTrechosRetos() + " mm");
        } else {
            handler.addString(linha, 3, "N�o haver� trechos retos nas extremidades da curva");
        }
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Corte:");
        if (oc.getCorte() == 0) {
            handler.addString(linha, 3, "Oxi-acetil�nico");
        } else if (oc.getCorte() == 1) {
            handler.addString(linha, 3, "Serra");
        } else {
            handler.addString(linha, 3, "Sem corte");
        }
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Bisel:");
        if (oc.getBiselOption() == 0) {
            handler.addString(linha, 3, "Conforme ASME B 16.25/ED.97");
        } else if (oc.getBiselOption() == 1) {
            handler.addString(linha, 3, "Sem Bisel");
        } else {
            handler.addString(linha, 3, oc.getBisel());
        }
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ") Inspe��o:");
        if (oc.getNivelInspecao() == 0) {
            handler.addString(linha, 3, "Conforme nossa rotina RI-7.5.1.1, n�vel I");
        } else {
            handler.addString(linha, 3, "Conforme nossa rotina RI-7.5.1.1, n�vel II");
        }
        linha = linha + 1;
        ContaCompl = ContaCompl + 1;
        handler.addString(linha, 1, ContaCompl + ")  Toler�ncia de fabrica��o:");
        linha = linha + 1;
        Tolerancia tolerancia = oc.getTolerancia();
        if (tolerancia.getRaioMaiorMil() > 0 || tolerancia.getRaioMenorMil() > 0) {
            handler.addString(linha, 1, "- Raio:");
            if (tolerancia.getRaioMenorMil() > 0) {
                handler.addString(linha, 2, "R < 1000 mm");
                handler.addString(linha, 3, tolerancia.getRaioMenorMil().toString());
                handler.addString(linha, 4, "mm");
                linha = linha + 1;
            }
            if (tolerancia.getRaioMaiorMil() > 0) {
                handler.addString(linha, 2, "R > 1000 mm");
                handler.addString(linha, 3, tolerancia.getRaioMaiorMil().toString());
                handler.addString(linha, 4, "mm");
                linha = linha + 1;
            }
        }
        if (tolerancia.getAngulo() > 0) {
            handler.addString(linha, 2, "- �ngulo:");
            handler.addString(linha, 3, tolerancia.getAngulo().toString());
            handler.addString(linha, 4, "''");
            linha = linha + 1;
        }
        if (tolerancia.getOvalizacaoPercent() > 0) {
            handler.addString(linha, 2, "- Ovaliza��o (OV%):");
            handler.addString(linha, 3, tolerancia.getOvalizacaoPercent().toString());
            handler.addString(linha, 4, "%");
            linha = linha + 1;
        }
        if (tolerancia.getPasso() > 0) {
            handler.addString(linha, 2, "- Passo:");
            handler.addString(linha, 3, tolerancia.getPasso().toString());
            handler.addString(linha, 4, "mm");
            linha = linha + 1;
        }
        if (tolerancia.getCentroFace() > 0) {
            handler.addString(linha, 2, "- Centro a face:");
            handler.addString(linha, 3, tolerancia.getCentroFace().toString());
            handler.addString(linha, 4, "mm");
            linha = linha + 1;
        }
        if (tolerancia.getRedEspessura() > 0) {
            if (oc.getFornecimentoMaterial() == 0) {
                handler.addString(linha, 1, "- Red. de espessura (RE%):");
                handler.addString(linha, 3, tolerancia.getRedEspessura().toString());
                handler.addString(linha, 4, "%");
                linha = linha + 1;
            } else {
                handler.addString(linha, 1, "- Espessura da curva: �12,5% da espessura nominal");
                linha = linha + 1;
            }
        }
        Integer proxlinha = linha;
        if (tolerancia.getRedEspessura() > 0) {
            if (oc.getFornecimentoMaterial() == 0) {
                handler.addString(proxlinha, 6, "RE% = (To-T)/To x 100%, onde:");
                handler.addString(proxlinha, 10, "T = esp. ap�s curvamento");
                proxlinha++;
                handler.addString(proxlinha, 10, "To = esp. do tubo reto");
                proxlinha++;
            }
        }
        if (tolerancia.getOvalizacaoPercent() > 0) {
            if (tolerancia.getRedEspessura() > 0) {
                proxlinha++;
                handler.addString(proxlinha, 6, "OV% = (A-B)/D x 100%, onde:");
                handler.addString(proxlinha, 10, "A = eixo maior");
                proxlinha++;
                handler.addString(proxlinha, 10, "B = eixo menor");
                proxlinha++;
                handler.addString(proxlinha, 10, "D = di�metro externo");
                proxlinha++;
                handler.addString(proxlinha, 10, "       nominal");
                proxlinha++;
            }
        }
        ContaCompl = ContaCompl + 1;
        MaterialNecessario material = oc.getMaterialNecessario();
        if (material != null) {
            handler.addString(linha, 1, ContaCompl + ") Quantidade de material necess�ria:");
            String texto = material.getTexto();
            String textos[] = texto.split("\n");
            int iterator = 0;
            while (iterator < textos.length) {
                handler.addString(linha, 3, textos[iterator]);
                linha++;
                iterator++;
            }
        }
    }

    public int getItensPorPagina() {
        return 0;
    }

    public void setItemFim(int index) {
        this.itemFim = index;
    }

    public void setItemInit(int index) {
        this.itemInit = index;
    }

    private int itemFim;

    private int itemInit;

    public String getPath() {
        return "orcamentopaginainicial";
    }
}
