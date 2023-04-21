package com.meufinanceiro.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import com.meufinanceiro.business.CarteiraBusiness;
import com.meufinanceiro.business.exception.BusinessException;
import com.meufinanceiro.business.search.CarteiraSearch;
import com.meufinanceiro.entity.Carteira;

@Name("carteiraList")
public class CarteiraList
		extends ActionList<Carteira>
{
	private static final long serialVersionUID = -1158021935596540445L;

	@DataModel
	private List<Carteira> carteiras = new ArrayList<Carteira>();

	@DataModelSelection
	@Out(required = false)
	protected Carteira carteira;

	@In(create = true)
	protected CarteiraSearch carteiraSearch;
	
	@In(create = true)
	protected CarteiraBusiness carteiraBusiness;

	@Override
	public void delete(Carteira carteira)
	{
		try
		{
			carteiraBusiness.delete(carteira);
			retrieveList();
		}
		catch (BusinessException e)
		{
			e.printStackTrace();
		}
	}

	@Create
	public void onCreate()
	{
		retrieveList();
	}

	@Override
	public void retrieveList()
	{
		try
		{
			setCarteiras(carteiraSearch.findAll());
		}
		catch (final BusinessException e)
		{
			e.printStackTrace();
		}
	}

	public void setCarteiras(List<Carteira> carteiras)
	{
		this.carteiras = carteiras;
	}

	public List<Carteira> getCarteiras()
	{
		return carteiras;
	}

}