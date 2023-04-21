package com.javaPattern.AbstractFactory;

public class MySQLFactory extends AbstractFactory{

	@Override
	DAO1 DBdelete() {
		// TODO Auto-generated method stub
		return new MySQLDAO1();
	}

	@Override
	DAO2 DBinsert() {
		// TODO Auto-generated method stub
		return new MySQLDAO2();
	}

}
