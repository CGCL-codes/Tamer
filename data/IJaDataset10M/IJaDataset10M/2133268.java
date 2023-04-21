package com.angis.fx.activity.search;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.angis.fx.activity.R;
import com.angis.fx.data.ChangsuoInformation;

/**
 * @author Han
 *
 */
public class ChangsuoDetailsActivity extends Activity {

    private ChangsuoInformation mChangsuoInfo;

    private TextView mNameText;

    private TextView mAddressText;

    private TextView mAreaText;

    private TextView mTypeText;

    private TextView mManagerText;

    private TextView mContactText;

    private TextView mPermitCodeText;

    private TextView mPermitWordText;

    private TextView mUnitType;

    private TextView mUnicode;

    private TextView mBusinessCondition;

    private TextView mDetailsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changsuodetails);
        mChangsuoInfo = (ChangsuoInformation) getIntent().getExtras().get("selectedcs");
        initActivity();
        setValue();
    }

    private void initActivity() {
        mNameText = (TextView) findViewById(R.id.csdetailsname);
        mAddressText = (TextView) findViewById(R.id.csdetailsaddress);
        mAreaText = (TextView) findViewById(R.id.csdetailsarea);
        mTypeText = (TextView) findViewById(R.id.csdetailstype);
        mManagerText = (TextView) findViewById(R.id.csdetailspersonincharge);
        mContactText = (TextView) findViewById(R.id.csdetailscontact);
        mPermitCodeText = (TextView) findViewById(R.id.csdetailslicenseno);
        mPermitWordText = (TextView) findViewById(R.id.csdetailslicenseword);
        mUnitType = (TextView) findViewById(R.id.csdetailsdevicecount);
        mUnicode = (TextView) findViewById(R.id.csdetailsunicode);
        mBusinessCondition = (TextView) findViewById(R.id.csdetailsbusinesscondition);
        mDetailsStatus = (TextView) findViewById(R.id.csdetailsstatus);
    }

    private void setValue() {
        if (mChangsuoInfo != null) {
            mNameText.setText(mChangsuoInfo.getTitle());
            mAddressText.setText(mChangsuoInfo.getAddress());
            mAreaText.setText(mChangsuoInfo.getArea());
            mTypeText.setText(mChangsuoInfo.getAreaType());
            mManagerText.setText(mChangsuoInfo.getPersonInCharge());
            mContactText.setText(mChangsuoInfo.getTelefon());
            mPermitCodeText.setText(mChangsuoInfo.getPermitCode());
            mPermitWordText.setText(mChangsuoInfo.getPermitWord());
            mUnitType.setText(mChangsuoInfo.getUnitType());
            mUnicode.setText(mChangsuoInfo.getCode());
            mBusinessCondition.setText(mChangsuoInfo.getManagePos());
            mDetailsStatus.setText(mChangsuoInfo.getHyCode());
        }
    }
}
