package com.ecomponentes.formularios.consultas.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.ecomponentes.dao.BaseDAOHibernate;

public class ConsultaContatoDAO extends BaseDAOHibernate {

    public String formataData(String data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = formatter.parse(data);
            data = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List getContatos(String contato, String empresa, String investidor, String invOR, String setorInv, String finder, String finOR, String setorFinder, String regiaoFinder, String expert, String expOR, String setorExpert, String mailing, String mailOR, String holdMailing, String holdOR, String antiSpam, String spOR, String aniversarioInicial, String aniversarioFinal, String dayOR, String responsavel, String resOR, String network, String netOR, int inicio, int fim) {
        String sql = "from TbContato as contato  ";
        String sqlEmpresa = new String();
        String sqlSetorExpert = new String();
        String sqlSetorFinder = new String();
        String sqlSetorInv = new String();
        String where = new String();
        String whereContato = new String();
        String whereEmpresa = new String();
        String whereSetorExpert = new String();
        String whereSetorFinder = new String();
        String whereSetorInv = new String();
        String whereAniversario = new String();
        if (invOR.equals("")) invOR = " AND ";
        if (finOR.equals("")) finOR = " AND ";
        if (expOR.equals("")) expOR = " AND ";
        if (mailOR.equals("")) mailOR = " AND ";
        if (holdOR.equals("")) holdOR = " AND ";
        if (spOR.equals("")) spOR = " AND ";
        if (dayOR.equals("")) dayOR = " AND ";
        if (resOR.equals("")) resOR = " AND ";
        if (netOR.equals("")) netOR = " AND ";
        if (!contato.equals("")) {
            whereContato = " (contato.idContato = " + contato.replaceAll(",", " OR contato.idContato = ") + ") ";
        }
        if (!investidor.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += invOR;
            }
            investidor = " contato.investidorFinanceiro = '" + investidor + "'";
            if (!setorInv.equals("")) {
                sqlSetorInv = ",TbSetorContatoInvestidor as setorInvestidor ";
                whereSetorFinder = " (" + investidor + " AND " + setorInv.replaceAll("TESTE.CONDICAO", "setorInvestidor.tbContato.idContato = contato.idContato") + ") ";
            } else {
                whereContato += " " + investidor + " ";
            }
        }
        if (!finder.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += finOR;
            }
            whereContato += " contato.finder = '" + finder + "'";
            if (!setorFinder.equals("")) {
                sqlSetorFinder = ",TbSetorContatoFinder as setorFinder ";
                whereSetorFinder = " AND " + setorFinder.replaceAll("TESTE.CONDICAO", "setorFinder.tbContato.idContato = contato.idContato");
            }
            if (!regiaoFinder.equals("")) {
                whereContato += " AND contato.idRegiao = " + regiaoFinder;
            }
        }
        if (!expert.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += expOR;
            }
            whereContato += " contato.industryExpert = '" + expert + "'";
            if (!setorExpert.equals("")) {
                sqlSetorExpert = ",TbSetorContatoExpert as setorExpert ";
                whereSetorExpert = " AND " + setorExpert.replaceAll("TESTE.CONDICAO", "setorExpert.tbContato.idContato = contato.idContato");
            }
        }
        if (!mailing.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += mailOR;
            }
            whereContato += " contato.recebeMailing = '" + mailing + "'";
        }
        if (!holdMailing.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += holdOR;
            }
            whereContato += " contato.holdMailing = '" + holdMailing + "'";
        }
        if (!antiSpam.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += spOR;
            }
            whereContato += " contato.antiSpawn = '" + antiSpam + "'";
        }
        if (!aniversarioInicial.equals("")) {
            aniversarioInicial = formataData(aniversarioInicial);
            whereAniversario += " contato.aniversario >= '" + aniversarioInicial + "'";
        }
        if (!aniversarioFinal.equals("")) {
            aniversarioFinal = formataData(aniversarioFinal);
            if (!whereAniversario.equals("")) {
                whereAniversario += " AND ";
            }
            whereAniversario += " contato.aniversario <= '" + aniversarioFinal + "'";
        }
        if (!whereAniversario.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += dayOR;
            }
            whereContato += " (" + whereAniversario + ") ";
        }
        if (!responsavel.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += resOR;
            }
            whereContato += " contato.idContatoInterno = " + responsavel + " ";
        }
        if (!network.equals("")) {
            if (!whereContato.equals("")) {
                whereContato += netOR;
            }
            whereContato += " contato.network = '" + network + "'";
        }
        if (!empresa.equals("")) {
            if (!whereContato.equals("")) {
                whereEmpresa += " AND ";
            }
            sqlEmpresa = ", TbEmpresaHasContato as empresa ";
            whereEmpresa += " ((empresa.tbEmpresa.idEmpresa = " + empresa.replaceAll(",", " OR empresa.tbEmpresa.idEmpresa =") + " )AND empresa.tbContato.idContato = contato.idContato )";
        }
        if (!whereContato.equals("") || !whereEmpresa.equals("") || !whereSetorExpert.equals("") || !whereSetorFinder.equals("") || !whereSetorInv.equals("")) {
            where = " where " + whereContato + whereEmpresa + whereSetorExpert + whereSetorFinder + whereSetorInv;
        }
        sql += sqlEmpresa + sqlSetorExpert + sqlSetorFinder + sqlSetorInv + where + " order by contato.nome LIMIT " + inicio + "," + fim;
        return getObjects(sql);
    }

    public List getContatosVw(String contato, String investidor, String invOR, String setorInv, String finder, String finOR, String setorFinder, String expert, String expOR, String setorExpert) {
        String sql = "from ConsultaContatosWv as contato";
        String where = new String();
        if (invOR.equals("")) invOR = " AND ";
        if (finOR.equals("")) finOR = " AND ";
        if (expOR.equals("")) expOR = " AND ";
        if (!contato.equals("")) {
            where = " (contato.idContato = " + contato.replaceAll(",", " OR contato.idContato = ") + ") ";
        }
        if (!investidor.equals("")) {
            if (!where.equals("")) {
                where += invOR;
            }
            investidor = " contato.investidorFinanceiro = '" + investidor + "'";
            if (!setorInv.equals("")) {
                where += " (" + investidor + " AND " + setorInv.replaceAll("AND TESTE.CONDICAO", " ").replaceAll("setorInvestidor.tbSetor.idSetor", " contato.idSetorInv ").replaceAll("setorInvestidor.tbSubSetor.idSubSetor", "contato.idSubInvestidor") + ")";
            } else {
                where += " " + investidor + " ";
            }
        }
        if (!finder.equals("")) {
            if (!where.equals("")) {
                where += finOR;
            }
            finder = " contato.finder = '" + finder + "'";
            if (!setorFinder.equals("")) {
                where += "(" + finder + " AND " + setorFinder.replaceAll("AND TESTE.CONDICAO", " ").replaceAll("setorFinder.tbSetor.idSetor", "contato.idSetorFinder ").replaceAll("setorFinder.tbSubSetor.idSubSetor", "contato.idSubFinder") + ")";
            } else {
                where += " " + finder + " ";
            }
        }
        if (!expert.equals("")) {
            if (!where.equals("")) {
                where += expOR;
            }
            expert = " contato.industryExpert = '" + expert + "'";
            if (!setorExpert.equals("")) {
                where += "(" + expert + " AND " + setorExpert.replaceAll("AND TESTE.CONDICAO", " ").replaceAll("setorExpert.tbSetor.idSetor", " contato.idSetorExpert ").replaceAll("setorExpert.tbSubSetor.idSubSetor", "contato.idSubExpert") + ")";
            } else {
                where += " " + expert + " ";
            }
        }
        if (!where.equals("")) {
            where = " where " + where;
        }
        sql += where;
        return getObjects(sql);
    }

    public List getContatosProjeto(String empresa, String perfil, String status) {
        String sql = "from TbEmpresa as emp1, TbEmpresa as emp2, TbPerfilHasEmpresa as perfilEmpresa, TbPerfilHasEmpresaHasEmpresa as perfilEmpresaEmpresa," + "TbEmpresaHasContato as empresaContato, TbContato as contato, TbPerfil as perfil ";
        String where = new String();
        if (!empresa.equals("")) {
            where = " emp1.idEmpresa = " + empresa + " ";
        }
        where += " and emp1.idEmpresa = perfilEmpresa.tbEmpresa.idEmpresa" + " and perfilEmpresa.tbPerfil.idPerfil = perfil.idPerfil";
        if (!perfil.equals("")) {
            if (!where.equals("")) {
                where += " AND ";
            }
            where += " perfil.idPerfil = " + perfil + " ";
        }
        if (!where.equals("")) {
            where = " WHERE " + where;
        }
        where += " AND perfil.idPerfil = perfilEmpresaEmpresa.tbPerfil.idPerfil" + " AND perfilEmpresaEmpresa.tbEmpresa.idEmpresa = emp2.idEmpresa" + " AND emp2.idEmpresa = empresaContato.tbEmpresa.idEmpresa" + " AND empresaContato.tbContato.idContato = contato.idContato";
        sql += where + " order by emp1.nomeEmpresa, perfil.nomePerfil, perfilEmpresaEmpresa.isactive, emp2.nomeEmpresa," + " contato.nome";
        return getObjects(sql);
    }

    public List getContatosProjetoVelho(String empresa, String perfil, String status) {
        String sql = "from ContatoProjetoVw as contatoProjeto ";
        String where = new String();
        if (!empresa.equals("")) {
            where = " contatoProjeto.idEmpresa = " + empresa + " ";
        }
        if (!perfil.equals("")) {
            if (!where.equals("")) {
                where += " AND ";
            }
            where += " contatoProjeto.idPerfil = " + perfil + " ";
        }
        if (!status.equals("")) {
            if (!where.equals("")) {
                where += " AND ";
            }
            where += " contatoProjeto.perfilAtivo = " + status + " ";
        }
        if (!where.equals("")) {
            where = " WHERE " + where;
        }
        sql += where + " order by contatoProjeto.nomeEmpresa, contatoProjeto.nomePerfil, contatoProjeto.perfilAtivo, contatoProjeto.empresaNome";
        return getObjects(sql);
    }
}
