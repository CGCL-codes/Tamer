package org.omg.TerminologyServices;

/**
 * Interface definition: CodingSchemeVersion.
 * 
 * @author OpenORB Compiler
 */
public class _CodingSchemeVersionStub extends org.omg.CORBA.portable.ObjectImpl implements CodingSchemeVersion {

    static final String[] _ids_list = { "IDL:omg.org/TerminologyServices/CodingSchemeVersion:1.0", "IDL:omg.org/TerminologyServices/CodingSchemeVersionAttributes:1.0" };

    public String[] _ids() {
        return _ids_list;
    }

    private static final Class _opsClass = org.omg.TerminologyServices.CodingSchemeVersionOperations.class;

    /**
     * Operation get_syntactic_types
     */
    public org.omg.TerminologyServices.QualifiedCode[] get_syntactic_types() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_syntactic_types", true);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.QualifiedCode[] _arg_ret = org.omg.TerminologyServices.SyntacticTypeIdSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_syntactic_types", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_syntactic_types();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_source_term_usages
     */
    public org.omg.TerminologyServices.QualifiedCode[] get_source_term_usages() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_source_term_usages", true);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.QualifiedCode[] _arg_ret = org.omg.TerminologyServices.SourceTermUsageIdSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_source_term_usages", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_source_term_usages();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_scheme_source_ids
     */
    public org.omg.TerminologyServices.QualifiedCode[] get_scheme_source_ids() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_scheme_source_ids", true);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.QualifiedCode[] _arg_ret = org.omg.TerminologyServices.SourceIdSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_scheme_source_ids", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_scheme_source_ids();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_usage_contexts
     */
    public org.omg.TerminologyServices.QualifiedCode[] get_usage_contexts() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_usage_contexts", true);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.QualifiedCode[] _arg_ret = org.omg.TerminologyServices.UsageContextIdSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_usage_contexts", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_usage_contexts();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation list_concepts
     */
    public void list_concepts(int how_many, org.omg.TerminologyServices.ConceptInfoSeqHolder concept_info_seq, org.omg.TerminologyServices.ConceptInfoIterHolder concept_info_iter) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("list_concepts", true);
                    _output.write_ulong(how_many);
                    _input = this._invoke(_output);
                    concept_info_seq.value = org.omg.TerminologyServices.ConceptInfoSeqHelper.read(_input);
                    concept_info_iter.value = org.omg.TerminologyServices.ConceptInfoIterHelper.read(_input);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("list_concepts", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    _self.list_concepts(how_many, concept_info_seq, concept_info_iter);
                    return;
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation is_valid_concept
     */
    public boolean is_valid_concept(String a_code) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("is_valid_concept", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("is_valid_concept", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.is_valid_concept(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_definitions
     */
    public org.omg.TerminologyServices.Definition[] get_definitions(String a_code) throws org.omg.TerminologyServices.UnknownCode {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_definitions", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.Definition[] _arg_ret = org.omg.TerminologyServices.DefinitionSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_definitions", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_definitions(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_preferred_definition
     */
    public org.omg.TerminologyServices.Definition get_preferred_definition(String a_code) throws org.omg.TerminologyServices.UnknownCode {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_preferred_definition", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.Definition _arg_ret = org.omg.TerminologyServices.DefinitionHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_preferred_definition", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_preferred_definition(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_comments
     */
    public org.omg.TerminologyServices.Comment[] get_comments(String a_code) throws org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.UnknownCode {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_comments", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.Comment[] _arg_ret = org.omg.TerminologyServices.CommentSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.NotImplementedHelper.id())) {
                        throw org.omg.TerminologyServices.NotImplementedHelper.read(_exception.getInputStream());
                    }
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_comments", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_comments(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_instructions
     */
    public org.omg.TerminologyServices.Instruction[] get_instructions(String a_code) throws org.omg.TerminologyServices.NotImplemented, org.omg.TerminologyServices.UnknownCode {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_instructions", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    org.omg.TerminologyServices.Instruction[] _arg_ret = org.omg.TerminologyServices.InstructionSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.NotImplementedHelper.id())) {
                        throw org.omg.TerminologyServices.NotImplementedHelper.read(_exception.getInputStream());
                    }
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_instructions", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_instructions(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_all_text
     */
    public String[] get_all_text(String a_code) throws org.omg.TerminologyServices.UnknownCode {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_all_text", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    String[] _arg_ret = org.omg.TerminologyServices.IntlStringSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_all_text", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_all_text(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_preferred_text
     */
    public String get_preferred_text(String a_code) throws org.omg.TerminologyServices.UnknownCode, org.omg.TerminologyServices.NoPreferredText {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_preferred_text", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    _input = this._invoke(_output);
                    String _arg_ret = org.omg.TerminologyServices.IntlStringHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    if (_exception_id.equals(org.omg.TerminologyServices.NoPreferredTextHelper.id())) {
                        throw org.omg.TerminologyServices.NoPreferredTextHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_preferred_text", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_preferred_text(a_code);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_text_for_context
     */
    public String get_text_for_context(String a_code, org.omg.TerminologyServices.QualifiedCode[] context_ids) throws org.omg.TerminologyServices.UnknownCode, org.omg.TerminologyServices.NoTextLocated {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_text_for_context", true);
                    org.omg.TerminologyServices.ConceptCodeHelper.write(_output, a_code);
                    org.omg.TerminologyServices.UsageContextIdSeqHelper.write(_output, context_ids);
                    _input = this._invoke(_output);
                    String _arg_ret = org.omg.TerminologyServices.IntlStringHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.UnknownCodeHelper.id())) {
                        throw org.omg.TerminologyServices.UnknownCodeHelper.read(_exception.getInputStream());
                    }
                    if (_exception_id.equals(org.omg.TerminologyServices.NoTextLocatedHelper.id())) {
                        throw org.omg.TerminologyServices.NoTextLocatedHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_text_for_context", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_text_for_context(a_code, context_ids);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation get_concepts_by_text
     */
    public String[] get_concepts_by_text(String text) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("get_concepts_by_text", true);
                    _output.write_string(text);
                    _input = this._invoke(_output);
                    String[] _arg_ret = org.omg.TerminologyServices.ConceptCodeSeqHelper.read(_input);
                    return _arg_ret;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_concepts_by_text", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    return _self.get_concepts_by_text(text);
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation match_concepts_by_string
     */
    public void match_concepts_by_string(String match_string, int how_many, org.omg.TerminologyServices.WeightedResultSeqHolder weighted_results, org.omg.TerminologyServices.WeightedResultsIterHolder weighted_result_iter) throws org.omg.TerminologyServices.NotImplemented {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("match_concepts_by_string", true);
                    org.omg.TerminologyServices.IntlStringHelper.write(_output, match_string);
                    _output.write_ulong(how_many);
                    _input = this._invoke(_output);
                    weighted_results.value = org.omg.TerminologyServices.WeightedResultSeqHelper.read(_input);
                    weighted_result_iter.value = org.omg.TerminologyServices.WeightedResultsIterHelper.read(_input);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.NotImplementedHelper.id())) {
                        throw org.omg.TerminologyServices.NotImplementedHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("match_concepts_by_string", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    _self.match_concepts_by_string(match_string, how_many, weighted_results, weighted_result_iter);
                    return;
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation match_concepts_by_keywords
     */
    public void match_concepts_by_keywords(String[] keywords, int how_many, org.omg.TerminologyServices.WeightedResultSeqHolder weighted_results, org.omg.TerminologyServices.WeightedResultsIterHolder weighted_results_iter) throws org.omg.TerminologyServices.NotImplemented {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("match_concepts_by_keywords", true);
                    org.omg.TerminologyServices.OrderedIntlStringSeqHelper.write(_output, keywords);
                    _output.write_ulong(how_many);
                    _input = this._invoke(_output);
                    weighted_results.value = org.omg.TerminologyServices.WeightedResultSeqHelper.read(_input);
                    weighted_results_iter.value = org.omg.TerminologyServices.WeightedResultsIterHelper.read(_input);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _exception) {
                    String _exception_id = _exception.getId();
                    if (_exception_id.equals(org.omg.TerminologyServices.NotImplementedHelper.id())) {
                        throw org.omg.TerminologyServices.NotImplementedHelper.read(_exception.getInputStream());
                    }
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("match_concepts_by_keywords", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionOperations) _so.servant;
                try {
                    _self.match_concepts_by_keywords(keywords, how_many, weighted_results, weighted_results_iter);
                    return;
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for coding_scheme_id attribute
     * @return the attribute value
     */
    public org.omg.NamingAuthority.AuthorityId coding_scheme_id() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_coding_scheme_id", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.CodingSchemeIdHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_coding_scheme_id", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.coding_scheme_id();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for version_id attribute
     * @return the attribute value
     */
    public String version_id() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_version_id", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.VersionIdHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_version_id", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.version_id();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for language_id attribute
     * @return the attribute value
     */
    public String language_id() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_language_id", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.LanguageIdHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_language_id", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.language_id();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for is_default_version attribute
     * @return the attribute value
     */
    public boolean is_default_version() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_is_default_version", true);
                    _input = this._invoke(_output);
                    return _input.read_boolean();
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_is_default_version", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.is_default_version();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for is_complete_scheme attribute
     * @return the attribute value
     */
    public boolean is_complete_scheme() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_is_complete_scheme", true);
                    _input = this._invoke(_output);
                    return _input.read_boolean();
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_is_complete_scheme", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.is_complete_scheme();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for coding_scheme_version_if attribute
     * @return the attribute value
     */
    public org.omg.TerminologyServices.CodingSchemeVersion coding_scheme_version_if() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_coding_scheme_version_if", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.CodingSchemeVersionHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_coding_scheme_version_if", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.coding_scheme_version_if();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for presentation_if attribute
     * @return the attribute value
     */
    public org.omg.TerminologyServices.PresentationAccess presentation_if() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_presentation_if", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.PresentationAccessHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_presentation_if", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.presentation_if();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for linguistic_group_if attribute
     * @return the attribute value
     */
    public org.omg.TerminologyServices.LinguisticGroupAccess linguistic_group_if() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_linguistic_group_if", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.LinguisticGroupAccessHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_linguistic_group_if", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.linguistic_group_if();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for systemization_if attribute
     * @return the attribute value
     */
    public org.omg.TerminologyServices.SystemizationAccess systemization_if() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_systemization_if", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.SystemizationAccessHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_systemization_if", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.systemization_if();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Read accessor for advanced_query_if attribute
     * @return the attribute value
     */
    public org.omg.TerminologyServices.AdvancedQueryAccess advanced_query_if() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _input = null;
                try {
                    org.omg.CORBA.portable.OutputStream _output = this._request("_get_advanced_query_if", true);
                    _input = this._invoke(_output);
                    return org.omg.TerminologyServices.AdvancedQueryAccessHelper.read(_input);
                } catch (final org.omg.CORBA.portable.RemarshalException _exception) {
                    continue;
                } catch (final org.omg.CORBA.portable.ApplicationException _exception) {
                    final String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
                } finally {
                    this._releaseReply(_input);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("_get_advanced_query_if", _opsClass);
                if (_so == null) continue;
                org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations _self = (org.omg.TerminologyServices.CodingSchemeVersionAttributesOperations) _so.servant;
                try {
                    return _self.advanced_query_if();
                } finally {
                    _servant_postinvoke(_so);
                }
            }
        }
    }
}
