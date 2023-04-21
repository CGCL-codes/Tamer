package ${packageName}.pages;

import java.util.List;

import ${packageName}.domain.Bar;
import ${packageName}.domain.Foo;


public abstract class ListOfFoos extends TemplateBasePage {
	
	public abstract Foo getFoo();
	public abstract Foo getFooToBeDeleted();
	public abstract Foo getFooToAddBar();
	
	public abstract String getNewBarName();
	public abstract Double getNewBarPrice();

	public List<Foo> getFoos() {		
		return getPersistenceService().retrieveAllFoos();		
	}
	
	public void onDelete() {		
		getPersistenceService().delete( getFooToBeDeleted() );		
	}
	
	public void onNewBar() {
		getFooToAddBar().getFoosBars().add( 
				new Bar( getNewBarName(), getNewBarPrice()));
	}

}
