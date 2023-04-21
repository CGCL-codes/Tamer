package com.soft27.heavyLifting;

/**
This is an internal use class to hold properties if the
user supplied a Properties object instead of the PoolObjects
implementing the PoolObjectsProps interface.
<p>
Basically, this extends the BasicPoolObjectProps class and
adds mutator methods to all the attributes.

	protected		long			m_lExpireTime			= 0;
	protected		long			m_lTickleTime			= 0;
	protected		boolean			m_bValidateAtCheckIn	= false;
	protected		boolean			m_bValidateAtCheckOut	= false;
	protected		int				m_iMinPoolSize			= 5;
	protected		int				m_iMaxPoolSize			= 20;
	protected		int				m_iPoolSizeIncrement	= 1;
	protected		int				m_iPoolOverflowSize		= 0;
	protected		long			m_lShrinkageTime		= -1;
	protected		long			m_lMaxUses				= -1;

@author Ryan Krueger rskrueger@yahoo.com
@author Aaron Wishart adubyah@yahoo.com
@author David Proehl dproehl@yahoo.com
@since 8/1/2001
*/
public class PropsHolder extends BasicPoolObjectProps {

    /**
	Sets the amount of time that the object is valid before expiring.
	
	@param long - the amount of time in milliseconds before this object expires
	*/
    public void setExpireTime(long lExpireTime) {
        m_lExpireTime = lExpireTime;
    }

    /**
	Set the max amount of time between tickles.
	
	@param long - the max amount of time since the last checkin before a tickle must be performed on this object
	*/
    public void setTickleTime(long lTickleTime) {
        m_lTickleTime = lTickleTime;
    }

    /**
	If the object should be validated by calling the validate method
	just after the object is checked in.
	
	@param boolean - true if the objects should be validated
	*/
    public void setValidateAtCheckIn(boolean bValidateAtCheckIn) {
        m_bValidateAtCheckIn = bValidateAtCheckIn;
    }

    /**
	If the object should be validated by calling the validate method
	just before the object is checked out.
	
	@param boolean - true if the objects should be validated
	*/
    public void setValidateAtCheckOut(boolean bValidateAtCheckOut) {
        m_bValidateAtCheckOut = bValidateAtCheckOut;
    }

    /**
	Set the smallest size that the pool should be.
	
	@param int - the minimum pool size
	*/
    public void setMinPoolSize(int iMinPoolSize) {
        m_iMinPoolSize = iMinPoolSize;
    }

    /**
	Set the most objects that will be instantiated.
		
	@param int - the maximum pool size
	*/
    public void setMaxPoolSize(int iMaxPoolSize) {
        m_iMaxPoolSize = iMaxPoolSize;
    }

    /**
	Set how much to expand the pool each time that it must be increased.
	
	@param int - the pool size increment
	*/
    public void setPoolSizeIncrement(int iPoolSizeIncrement) {
        m_iPoolSizeIncrement = iPoolSizeIncrement;
    }

    /**
	Set the temporary object overage amount.
	
	@param int - the number of objects to temporarily go over the max pool
					size, zero if none should be allowed
	*/
    public void setOveragePoolSize(int iPoolOverflowSize) {
        m_iPoolOverflowSize = iPoolOverflowSize;
    }

    /**
	Set the shrinkage time.
	
	@param long - the amount of time in milliseconds before shrinking the pool
	*/
    public void setShrinkageTime(long lShrinkageTime) {
        m_lShrinkageTime = lShrinkageTime;
    }

    /**
	Set the maximum number of times an object may be used.
	
	@param long - the maximum number of times to use this object before discarding
	*/
    public void setMaxUses(long lMaxUses) {
        m_lMaxUses = lMaxUses;
    }
}
