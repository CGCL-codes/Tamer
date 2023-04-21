package pcgen.CharacterViewer.fragments;

import org.apache.commons.lang.StringUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import pcgen.android.Logger;
import pcgen.CharacterViewer.R;
import pcgen.CharacterViewer.resources.GameFolder;
import pcgen.CharacterViewer.resources.GameResourceCharacter;
import pcgen.CharacterViewer.resources.GameResourceNote;
import pcgen.CharacterViewer.resources.GameTypesNote;
import pcgen.CharacterViewer.utility.GameResourceCharacterJavascriptInterface;

public class CharacterTabContentFragment extends WebTabContentFragment {

    public CharacterTabContentFragment() {
    }

    public CharacterTabContentFragment(GameResourceCharacter resource) {
        super(resource);
    }

    public boolean dialogCancelCondition() {
        dialogCancel();
        return true;
    }

    public boolean dialogCancelDamageHeal() {
        dialogCancel();
        return true;
    }

    public boolean dialogCompleteCondition() {
        dialogCompleted();
        return true;
    }

    public boolean dialogCompleteDamageHeal() {
        dialogCompleted();
        return true;
    }

    public boolean dialogCancelNumberPicker() {
        dialogCancel();
        return true;
    }

    public boolean dialogCompleteNumberPicker(NumberPickerDialogFragment.Types type, int value, boolean add) {
        if (type == NumberPickerDialogFragment.Types.Initiative) getCharacterResource().setInitiativeCurrent(value);
        dialogCompleted();
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.tab_content_character;
    }

    protected GameFolder getGame() {
        return getCharacterResource().getFolder();
    }

    public GameResourceCharacter getCharacterResource() {
        return (GameResourceCharacter) getResource();
    }

    @Override
    protected void initCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.initCreate(inflater, container, savedInstanceState);
        setMenuShowRefresh(false);
        setMenuShowReload(true);
        setMenuShowRemove(true);
    }

    @Override
    protected void inflateView(View view) {
        try {
            super.inflateView(view);
            final CharacterTabContentFragment reference = this;
            getWebView().addJavascriptInterface(new GameResourceCharacterJavascriptInterface(this, getCharacterResource()), "Android");
            _buttonCombatMode = (Button) view.findViewById(R.id.button_character_combat_mode);
            _buttonCombatMode.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setCombatState();
                }
            });
            _buttonCombatRound = (Button) view.findViewById(R.id.button_character_combat_round);
            _buttonCombatRound.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setControlState();
                }
            });
            _buttonCombatToggle = (Button) view.findViewById(R.id.button_character_combat_toggle);
            _buttonCombatToggle.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setControlState();
                }
            });
            _buttonConditionAdd = (Button) view.findViewById(R.id.button_character_condition_add);
            _buttonConditionAdd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    ConditionDialogFragment.newInstance(reference, ConditionDialogFragment.Types.Add);
                }
            });
            _buttonConditionRemove = (Button) view.findViewById(R.id.button_character_condition_remove);
            _buttonConditionRemove.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    ConditionDialogFragment.newInstance(reference, ConditionDialogFragment.Types.Remove);
                }
            });
            _buttonDamageAbility = (Button) view.findViewById(R.id.button_character_damage_ability);
            _buttonDamageAbility.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    DamageHealDialogFragment.newInstance(reference, DamageHealDialogFragment.Types.DamageAbility);
                }
            });
            _buttonDamageHealth = (Button) view.findViewById(R.id.button_character_damage_health);
            _buttonDamageHealth.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    DamageHealDialogFragment.newInstance(reference, DamageHealDialogFragment.Types.DamageHealth);
                }
            });
            _buttonHealAbility = (Button) view.findViewById(R.id.button_character_heal_ability);
            _buttonHealAbility.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    DamageHealDialogFragment.newInstance(reference, DamageHealDialogFragment.Types.HealAbility);
                }
            });
            _buttonHealHealth = (Button) view.findViewById(R.id.button_character_heal_health);
            _buttonHealHealth.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    DamageHealDialogFragment.newInstance(reference, DamageHealDialogFragment.Types.HealHealth);
                }
            });
            _buttonInitiative = (Button) view.findViewById(R.id.button_character_initiative);
            _buttonInitiative.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    enableControls(false);
                    NumberPickerDialogFragment.newInstance(reference, NumberPickerDialogFragment.Types.Initiative, R.string.title_game_type_initiative, 0, 0, 20);
                }
            });
            _layoutNotes = (ViewGroup) view.findViewById(R.id.layout_notes);
            _layoutNotes.setVisibility(View.GONE);
            _textNotesType = (TextView) view.findViewById(R.id.text_notes_type);
            _textNotes = (TextView) view.findViewById(R.id.input_notes);
            _textNotes.setVisibility(View.VISIBLE);
            _buttonNotesContacts = (Button) view.findViewById(R.id.button_notes_contacts);
            _buttonNotesContacts.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setNoteVisibility(GameTypesNote.Contacts);
                }
            });
            _buttonNotesEquipment = (Button) view.findViewById(R.id.button_notes_equipment);
            _buttonNotesEquipment.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setNoteVisibility(GameTypesNote.Equipment);
                }
            });
            _buttonNotesGeneral = (Button) view.findViewById(R.id.button_notes_general);
            _buttonNotesGeneral.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setNoteVisibility(GameTypesNote.General);
                }
            });
            _buttonNotesPlaces = (Button) view.findViewById(R.id.button_notes_places);
            _buttonNotesPlaces.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setNoteVisibility(GameTypesNote.Places);
                }
            });
            _buttonNotesResources = (Button) view.findViewById(R.id.button_notes_resources);
            _buttonNotesResources.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    setNoteVisibility(GameTypesNote.Resources);
                }
            });
            _buttonNotes = (Button) view.findViewById(R.id.button_notes);
            _buttonNotes.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    _layoutNotes.setVisibility(_layoutNotes.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    if (_layoutNotes.getVisibility() == View.VISIBLE) setNoteVisibility(GameTypesNote.General); else setNoteVisibility(0);
                }
            });
            setControlState();
        } catch (Throwable tr) {
            Logger.e(TAG, "initializeView", tr);
        }
    }

    @Override
    protected void onWebPageFinished(WebView view, String url) {
        updateWebView();
    }

    @Override
    protected void reloadResource() {
        try {
            getCharacterResource().reload();
            setControlState();
            updateWebView();
        } catch (Throwable tr) {
            Logger.e(TAG, "reloadResource", tr);
        }
    }

    @Override
    protected void removeResource() {
        try {
            getActivityEx().removeResource(getResource());
            setControlState();
            updateWebView();
        } catch (Throwable tr) {
            Logger.e(TAG, "removeResource", tr);
        }
    }

    private void dialogCancel() {
        enableControls(true);
    }

    private void dialogCompleted() {
        updateWebView();
        enableControls(true);
        getActivityEx().gameFolderSave();
    }

    private void enableControls(boolean enabled) {
        _buttonCombatMode.setEnabled(enabled);
        _buttonCombatRound.setEnabled(false);
        _buttonCombatToggle.setEnabled(false);
        _buttonConditionAdd.setEnabled(enabled);
        _buttonConditionRemove.setEnabled(enabled);
        _buttonDamageAbility.setEnabled(enabled);
        _buttonDamageHealth.setEnabled(enabled);
        _buttonHealAbility.setEnabled(enabled);
        _buttonHealHealth.setEnabled(enabled);
        _buttonDamageAbility.setEnabled(enabled);
        _buttonInitiative.setEnabled(enabled);
        _buttonNotes.setEnabled(enabled);
    }

    private void setNoteVisibility(int type) {
        _buttonNotesContacts.setEnabled(true);
        _buttonNotesEquipment.setEnabled(true);
        _buttonNotesGeneral.setEnabled(true);
        _buttonNotesPlaces.setEnabled(true);
        _buttonNotesResources.setEnabled(true);
        if (type == GameTypesNote.Contacts) _buttonNotesContacts.setEnabled(false); else if (type == GameTypesNote.Equipment) _buttonNotesEquipment.setEnabled(false); else if (type == GameTypesNote.General) _buttonNotesGeneral.setEnabled(false); else if (type == GameTypesNote.Places) _buttonNotesPlaces.setEnabled(false); else if (type == GameTypesNote.Resources) _buttonNotesResources.setEnabled(false);
        if ((_notesType != 0) && (_notesType != type)) {
            if (_notesType == GameTypesNote.Contacts) getCharacterResource().setNote(GameTypesNote.Contacts, _textNotes.getText().toString()); else if (_notesType == GameTypesNote.Equipment) getCharacterResource().setNote(GameTypesNote.Equipment, _textNotes.getText().toString()); else if (_notesType == GameTypesNote.General) getCharacterResource().setNote(GameTypesNote.General, _textNotes.getText().toString()); else if (_notesType == GameTypesNote.Places) getCharacterResource().setNote(GameTypesNote.Places, _textNotes.getText().toString()); else if (_notesType == GameTypesNote.Resources) getCharacterResource().setNote(GameTypesNote.Resources, _textNotes.getText().toString());
            getActivityEx().gameFolderSave();
        }
        _notesType = type;
        if (_notesType <= 0) {
            _textNotesType.setText("");
            return;
        }
        int name = R.string.blank;
        if (type == GameTypesNote.Contacts) name = R.string.text_notes_contacts; else if (type == GameTypesNote.Equipment) name = R.string.text_notes_equipment; else if (type == GameTypesNote.General) name = R.string.text_notes_general; else if (type == GameTypesNote.Places) name = R.string.text_notes_places; else if (type == GameTypesNote.Resources) name = R.string.text_notes_resources;
        GameResourceNote note = getCharacterResource().getNote(type);
        String value = (note != null ? note.getText() : "");
        _textNotes.setText(value);
        _textNotesType.setText(getString(name));
    }

    private void setCombatState() {
        GameResourceCharacter character = getCharacterResource();
        if (character.getCombatState() == GameResourceCharacter.CombatStates.Normal) character.setCombatState(GameResourceCharacter.CombatStates.FightingDefensively); else if (character.getCombatState() == GameResourceCharacter.CombatStates.FightingDefensively) character.setCombatState(GameResourceCharacter.CombatStates.TotalDefense); else if (character.getCombatState() == GameResourceCharacter.CombatStates.TotalDefense) character.setCombatState(GameResourceCharacter.CombatStates.Normal);
        getActivityEx().gameFolderSave();
        setControlState();
        updateWebView();
    }

    private void setControlState() {
        _textNotes.setText("");
        int text = R.string.button_character_combat_mode_normal;
        if (getCharacterResource().getCombatState() == GameResourceCharacter.CombatStates.FightingDefensively) text = R.string.button_character_combat_mode_defensively; else if (getCharacterResource().getCombatState() == GameResourceCharacter.CombatStates.TotalDefense) text = R.string.button_character_combat_mode_total;
        _buttonCombatMode.setText(text);
    }

    private void updateWebView() {
        getWebView().loadUrl("javascript:(function() { updateFromAndroid(); })()");
    }

    private int _notesType = 0;

    private TextView _textNotes;

    private TextView _textNotesType;

    private ViewGroup _layoutNotes;

    private Button _buttonCombatMode;

    private Button _buttonCombatRound;

    private Button _buttonCombatToggle;

    private Button _buttonConditionAdd;

    private Button _buttonConditionRemove;

    private Button _buttonDamageAbility;

    private Button _buttonDamageHealth;

    private Button _buttonHealAbility;

    private Button _buttonHealHealth;

    private Button _buttonNotes;

    private Button _buttonNotesContacts;

    private Button _buttonNotesEquipment;

    private Button _buttonNotesGeneral;

    private Button _buttonNotesPlaces;

    private Button _buttonNotesResources;

    private Button _buttonInitiative;

    private static final String TAG = CharacterTabContentFragment.class.getSimpleName();
}
