package com.meufinanceiro.action;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.FlushModeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.international.StatusMessage.Severity;

import com.meufinanceiro.business.MovimentacaoBusiness;
import com.meufinanceiro.business.exception.BusinessException;
import com.meufinanceiro.business.search.GrupoMovimentSearch;
import com.meufinanceiro.business.search.MovimentacaoSearch;
import com.meufinanceiro.entity.Carteira;
import com.meufinanceiro.entity.CentroCusto;
import com.meufinanceiro.entity.GrupoMoviment;
import com.meufinanceiro.entity.Movimentacao;

@Name("movimentacaoList")
@Scope(ScopeType.CONVERSATION)
public class MovimentacaoList
		extends ActionList<Movimentacao>
{
	private static final long serialVersionUID = 6345274773513621374L;

	private Long grupoMovimentId;

	@DataModel
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

	private Double totalMoviment;

	private Double totalMovimentPosterior;

	@DataModelSelection
	@Out(required = false)
	protected Movimentacao movimentacao;

	private Set<String> sortedCols = new TreeSet<String>();

	private Object sortOrder;

	private boolean iniRetrieveList;

	@SuppressWarnings("unused")
	private String stringFiltro;

	@Override
	public void delete(Movimentacao movimentacao)
	{
		try
		{
			((MovimentacaoBusiness) Component.getInstance("movimentacaoBusiness")).delete(movimentacao);
			retrieveList();
		}
		catch (BusinessException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	@Begin(join = true)
	public void retrieveList()
	{
		try
		{
			if (iniRetrieveList && grupoMovimentId != null)
			{
				try
				{
					GrupoMoviment grupoMoviment = ((GrupoMovimentSearch) Component
							.getInstance("grupoMovimentSearch")).findGrupoMovimentById(grupoMovimentId);
					getCriterios().clear();
					getCriterios().put("grupoMoviment", grupoMoviment);
				}
				catch (BusinessException e)
				{
					e.printStackTrace();
				}
			}

			if (getCriterios().size() != 0)
			{
				MovimentacaoSearch movimentacaoSearch = (MovimentacaoSearch) Component
						.getInstance("movimentacaoSearch");
				setMovimentacoes(movimentacaoSearch.search(getCriterios()));
				setIniRetrieveList(false);
			}
		}
		catch (final BusinessException e)
		{
			e.printStackTrace();
		}
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes)
	{
		this.movimentacoes = movimentacoes;
	}

	public List<Movimentacao> getMovimentacoes()
	{
		return movimentacoes;
	}

	public void setGrupoMovimentId(Long grupoMovimentId)
	{
		this.grupoMovimentId = grupoMovimentId;
	}

	public Long getGrupoMovimentId()
	{
		return grupoMovimentId;
	}

	public void setSortedCols(Set<String> sortedCols)
	{
		this.sortedCols = sortedCols;
	}

	public Set<String> getSortedCols()
	{
		return sortedCols;
	}

	public void setSortOrder(Object sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public Object getSortOrder()
	{
		return sortOrder;
	}

	@Begin(flushMode = FlushModeType.MANUAL)
	public String novoRegistro()
	{
		return "save";
	}

	public void putCentroCustoInMovimentacao(CentroCusto centroCusto)
	{
		getCriterios().put("centroCustoCodigo", centroCusto.getCodigo());
	}

	public boolean isIniRetrieveList()
	{
		return iniRetrieveList;
	}

	public void setIniRetrieveList(boolean iniRetrieveList)
	{
		this.iniRetrieveList = iniRetrieveList;
	}

	public void clearCriterios()
	{
		setCriterios(null);
	}

	public Double getTotalMovimentAnterior()
	{
		try {
			MovimentacaoSearch movimentacaoSearch = (MovimentacaoSearch) Component
					.getInstance("movimentacaoSearch");
			return movimentacaoSearch.getTotalMovimentAnterior(getCriterios());
		}
		catch (Exception e) {
			return 0.0;
		}
	}

	public void calcTotalMovimentacoes()
	{
		Double total = Double.valueOf(0d);
		for (Movimentacao movimentacao : getMovimentacoes())
		{
			total += movimentacao.getValor();
		}
		setTotalMoviment(total);
	}

	public void calcTotalMovimentPosterior()
	{
		setTotalMovimentPosterior(getTotalMovimentAnterior() + getTotalMoviment());
	}

	public void setTotalMoviment(Double totalMovimentacoes)
	{
		this.totalMoviment = totalMovimentacoes;
	}

	public Double getTotalMoviment()
	{
		calcTotalMovimentacoes();
		return totalMoviment;
	}

	public void setTotalMovimentPosterior(Double totalMovimentPosterior)
	{
		this.totalMovimentPosterior = totalMovimentPosterior;
	}

	public Double getTotalMovimentPosterior()
	{
		if (getTotalMovimentAnterior() != null && getTotalMoviment() != null)
			calcTotalMovimentPosterior();
		return totalMovimentPosterior;
	}

	public void changePeriod(boolean back)
	{
		if (getCriterios().get("dtMovtoMES") != null && getCriterios().get("dtMovtoANO") != null)
		{
			Integer dtMovtoMES = Integer.parseInt((String) getCriterios().get("dtMovtoMES"));
			Integer dtMovtoANO = Integer.parseInt((String) getCriterios().get("dtMovtoANO"));

			if (!back)
			{
				if (dtMovtoMES + 1 == 13)
				{
					dtMovtoMES = 1;
					dtMovtoANO += 1;
				}
				else
				{
					dtMovtoMES += 1;
				}
			}
			else
			{
				if (dtMovtoMES - 1 == 0)
				{
					dtMovtoMES = 12;
					dtMovtoANO -= 1;
				}
				else
				{
					dtMovtoMES -= 1;
				}
			}

			getCriterios().put("dtMovtoMES", dtMovtoMES.toString());
			getCriterios().put("dtMovtoANO", dtMovtoANO.toString());

		}
		else if (getCriterios().get("grupoMoviment") != null)
		{
			GrupoMoviment grupoMoviment = (GrupoMoviment) getCriterios().get("grupoMoviment");

			StringBuffer descricao = new StringBuffer(grupoMoviment.getDescricao());
			Integer mes = Integer.valueOf(descricao.substring(descricao.indexOf("/") - 2, descricao
					.indexOf("/")));
			Integer ano = Integer.valueOf(descricao.substring(descricao.indexOf("/") + 1, descricao
					.indexOf("/") + 5));

			if (!back)
			{
				if (mes + 1 == 13)
				{
					mes = 1;
					ano += 1;
				}
				else
				{
					mes += 1;
				}
			}
			else
			{
				if (mes - 1 == 0)
				{
					mes = 12;
					ano -= 1;
				}
				else
				{
					mes -= 1;
				}
			}

			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits(2);

			String descricaoNova = descricao.substring(0, descricao.indexOf("/") - 2) + nf.format(mes)
					+ "/" + ano + descricao.substring(descricao.indexOf("/") + 5, descricao.length());

			try
			{
				GrupoMovimentSearch search = (GrupoMovimentSearch) Component.getInstance(
						"grupoMovimentSearch", true);
				GrupoMoviment novo = search.findGrupoMovimentByDescricao(descricaoNova);

				if (novo == null)
				{
					mensagens.add(Severity.INFO, "Grupo de Movimentação inexistente");
					getCriterios().put("grupoMoviment", grupoMoviment);
				}
				else
				{
					getCriterios().put("grupoMoviment", novo);
				}
			}
			catch (BusinessException e)
			{
				e.printStackTrace();
			}
		}

		retrieveList();

	}

	public String getStringFiltro()
			throws ParseException
	{
		StringBuffer retorno = new StringBuffer("");

		if (getCriterios().get("carteira") != null)
		{
			retorno.append(((Carteira) getCriterios().get("carteira")).getDescricao());
		}

		if (getCriterios().get("dtMovtoMES") != null && getCriterios().get("dtMovtoANO") != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
			Date dtMovto = sdf.parse("01/" + (String) getCriterios().get("dtMovtoMES") + "/"
					+ (String) getCriterios().get("dtMovtoANO"));

			if (!retorno.equals(""))
				retorno.append(" - ");

			Calendar cal = Calendar.getInstance();
			cal.setTime(dtMovto);

			retorno.append(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "/"
					+ cal.get(Calendar.YEAR));
		}

		if (getCriterios().get("grupoMoviment") != null)
			retorno.append(((GrupoMoviment) getCriterios().get("grupoMoviment")).getDescricao());

		return retorno.toString();
	}

	public void setStringFiltro(String stringFiltro)
	{
		this.stringFiltro = stringFiltro;

	}

	public void putEsteMes()
	{
		Calendar cal = Calendar.getInstance();
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);

		getCriterios().put("dtMovtoMES", nf.format(cal.get(Calendar.MONTH) + 1));
		getCriterios().put("dtMovtoANO", Integer.toString(cal.get(Calendar.YEAR)));
	}

}